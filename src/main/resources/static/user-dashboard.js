const API = window.location.origin;

function getToken()    { return sessionStorage.getItem('token'); }
function getUsername() { return sessionStorage.getItem('username') || 'User'; }
function getRole()     { return sessionStorage.getItem('role') || 'USER'; }

if (!getToken() || getRole() !== 'USER') {
  sessionStorage.clear();
  window.location.href = 'user-portal.html';
}

document.getElementById('sidebarUsername').textContent = getUsername();
document.getElementById('topbarUsername').textContent = getUsername();

// Toast
const toastEl = document.getElementById('appToast');
const bsToast = new bootstrap.Toast(toastEl, { delay: 3200 });
function showToast(msg, type = 'success') {
  document.getElementById('toastMsg').textContent = msg;
  toastEl.className = 'toast align-items-center border-0 toast-' + type;
  bsToast.show();
}

// API helper
async function apiFetch(path, options = {}) {
  const res = await fetch(`${API}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${getToken()}`,
      ...(options.headers || {})
    }
  });
  if (res.status === 401 || res.status === 403) {
    sessionStorage.clear();
    window.location.href = 'index.html';
    return;
  }
  if (!res.ok) {
    let msg = `Error ${res.status}`;
    try { const d = await res.json(); msg = d.message || msg; } catch(e) {}
    throw new Error(msg);
  }
  return res.json();
}

// Status badge for asset status
function statusBadge(status) {
  const map = {
    'available': 'badge-available',
    'assigned': 'badge-assigned',
    'under maintenance': 'badge-maintenance',
    'retired': 'badge-retired',
  };
  const cls = map[(status || '').toLowerCase()] || 'badge-retired';
  return `<span class="badge-status ${cls}">${status || 'Unknown'}</span>`;
}

// Status badge for maintenance status
function maintStatusBadge(status) {
  const map = {
    'pending':     'badge-maintenance',
    'in progress': 'badge-assigned',
    'repaired':    'badge-available',
    'rejected':    'badge-retired',
  };
  const cls = map[(status || '').toLowerCase()] || 'badge-maintenance';
  return `<span class="badge-status ${cls}">${status || 'Pending'}</span>`;
}

// ── Section navigation ──────────────────────────────────────────
function showSection(name) {
  document.getElementById('section-myassets').classList.toggle('d-none', name !== 'myassets');
  document.getElementById('section-maintenance').classList.toggle('d-none', name !== 'maintenance');
  document.getElementById('nav-myassets').classList.toggle('active', name === 'myassets');
  document.getElementById('nav-maintenance').classList.toggle('active', name === 'maintenance');

  const titles = {
    myassets: ['My Assets', 'Assets currently assigned to you'],
    maintenance: ['Maintenance History', 'All maintenance requests you have submitted'],
  };
  document.getElementById('pageTitle').textContent = titles[name][0];
  document.querySelector('.page-breadcrumb').textContent = titles[name][1];

  if (name === 'myassets') loadMyAssets();
  if (name === 'maintenance') loadMaintenanceHistory();
}

document.getElementById('nav-myassets').addEventListener('click', (e) => { e.preventDefault(); showSection('myassets'); });
document.getElementById('nav-maintenance').addEventListener('click', (e) => { e.preventDefault(); showSection('maintenance'); });

// ── Maintenance modal (report from assets table) ────────────────
let maintModal;
function openMaintenanceModal(assetId, assetName) {
  document.getElementById('maintAssetId').value = assetId;
  document.getElementById('maintAssetName').value = assetName;
  document.getElementById('maintDesc').value = '';
  document.getElementById('maintDesc').classList.remove('is-invalid');
  if (!maintModal) maintModal = new bootstrap.Modal(document.getElementById('maintenanceModal'));
  maintModal.show();
}

document.getElementById('submitMaintenanceBtn').addEventListener('click', async () => {
  const desc = document.getElementById('maintDesc').value.trim();
  const payload = {
    asset: { id: parseInt(document.getElementById('maintAssetId').value) },
    description: desc
  };
  try {
    await apiFetch('/api/maintenance', { method: 'POST', body: JSON.stringify(payload) });
    maintModal.hide();
    showToast('Maintenance request submitted successfully.');
    loadMyAssets();
  } catch (e) {
    showToast(e.message || 'Failed to submit.', 'error');
  }
});

// ── My Assets ───────────────────────────────────────────────────
async function loadMyAssets() {
  const wrap = document.getElementById('assetsTableWrap');
  wrap.innerHTML = '<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>';
  try {
    const assignments = await apiFetch('/api/assignments/my');
    const total = assignments.length;
    const active = assignments.filter(a => a.asset && (a.asset.status || '').toLowerCase() === 'assigned').length;
    const maintenance = assignments.filter(a => a.asset && (a.asset.status || '').toLowerCase() === 'under maintenance').length;
    animateCount('stat-total', total);
    animateCount('stat-active', active);
    animateCount('stat-maintenance', maintenance);

    if (total === 0) {
      wrap.innerHTML = '<div class="empty-state"><i class="bi bi-inbox"></i><p>No assets assigned to you yet.</p></div>';
      return;
    }
    const rows = assignments.map(a => {
      const asset = a.asset || {};
      const assetName = (asset.name || '').replace(/'/g, "\\'");
      return `<tr>
        <td class="col-primary">${asset.name || '—'}</td>
        <td>${asset.serialno || '—'}</td>
        <td>${asset.category ? asset.category.name : '—'}</td>
        <td>${statusBadge(asset.status)}</td>
        <td>${a.assignedDate || '—'}</td>
        <td>${a.returnDate || '—'}</td>
        <td>
          <button class="btn-action btn-action-edit" title="Report Maintenance"
            onclick="openMaintenanceModal(${asset.id}, '${assetName}')">
            <i class="bi bi-tools"></i>
          </button>
        </td>
      </tr>`;
    }).join('');

    wrap.innerHTML = `
      <table class="data-table" role="table">
        <thead><tr>
          <th>Asset Name</th><th>Serial No.</th><th>Category</th>
          <th>Status</th><th>Assigned Date</th><th>Return Date</th><th>Action</th>
        </tr></thead>
        <tbody>${rows}</tbody>
      </table>`;

    document.getElementById('assetSearch').addEventListener('input', (e) => {
      const q = e.target.value.toLowerCase();
      wrap.querySelectorAll('tbody tr').forEach(row => {
        row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
      });
    });
  } catch (e) {
    wrap.innerHTML = '<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>Failed to load assets.</p></div>';
  }
}

// ── Maintenance History ─────────────────────────────────────────
async function loadMaintenanceHistory() {
  const wrap = document.getElementById('maintHistoryWrap');
  wrap.innerHTML = '<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>';
  try {
    const logs = await apiFetch('/api/maintenance/my');
    if (!logs || logs.length === 0) {
      wrap.innerHTML = '<div class="empty-state"><i class="bi bi-inbox"></i><p>No maintenance requests submitted yet.</p></div>';
      return;
    }
    const rows = logs.map(m => {
      const rejRow = (m.status || '').toLowerCase() === 'rejected' && m.rejectionMessage
        ? `<tr class="rejection-row">
            <td colspan="4" style="padding:8px 16px 12px;">
              <div style="background:rgba(239,68,68,0.08);border-left:3px solid #f87171;padding:8px 12px;border-radius:0 6px 6px 0;">
                <i class="bi bi-info-circle me-1 text-danger"></i>
                <span class="text-danger" style="font-size:0.85rem;">${m.rejectionMessage}</span>
              </div>
            </td>
           </tr>` : '';
      return `<tr>
        <td class="col-primary">${m.asset ? m.asset.name : '—'}</td>
        <td>${m.maintainanceDate || '—'}</td>
        <td>${m.description || '—'}</td>
        <td>${maintStatusBadge(m.status)}</td>
      </tr>${rejRow}`;
    }).join('');

    wrap.innerHTML = `
      <table class="data-table" role="table">
        <thead><tr>
          <th>Asset</th><th>Date</th><th>Description</th><th>Status</th>
        </tr></thead>
        <tbody>${rows}</tbody>
      </table>`;

    document.getElementById('maintSearch').addEventListener('input', (e) => {
      const q = e.target.value.toLowerCase();
      wrap.querySelectorAll('tbody tr:not(.rejection-row)').forEach(row => {
        const next = row.nextElementSibling;
        const show = row.textContent.toLowerCase().includes(q);
        row.style.display = show ? '' : 'none';
        if (next && next.classList.contains('rejection-row')) next.style.display = show ? '' : 'none';
      });
    });
  } catch (e) {
    wrap.innerHTML = '<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>Failed to load history.</p></div>';
  }
}

// ── Utilities ───────────────────────────────────────────────────
function animateCount(id, target) {
  const el = document.getElementById(id);
  if (!el) return;
  let val = 0;
  const step = Math.max(1, Math.ceil(target / 30));
  const timer = setInterval(() => {
    val = Math.min(val + step, target);
    el.textContent = val;
    if (val >= target) clearInterval(timer);
  }, 20);
}

function openSidebar()  {
  document.getElementById('sidebar').classList.add('open');
  document.getElementById('sidebarOverlay').classList.add('active');
}
function closeSidebar() {
  document.getElementById('sidebar').classList.remove('open');
  document.getElementById('sidebarOverlay').classList.remove('active');
}
document.getElementById('sidebarOpen').addEventListener('click', openSidebar);
document.getElementById('sidebarClose').addEventListener('click', closeSidebar);
document.getElementById('sidebarOverlay').addEventListener('click', closeSidebar);

document.getElementById('refreshBtn').addEventListener('click', () => {
  const btn = document.getElementById('refreshBtn');
  btn.classList.add('spinning');
  const active = document.getElementById('section-maintenance').classList.contains('d-none') ? 'myassets' : 'maintenance';
  (active === 'myassets' ? loadMyAssets() : loadMaintenanceHistory())
    .then(() => setTimeout(() => btn.classList.remove('spinning'), 600));
});

document.getElementById('logoutBtn').addEventListener('click', () => {
  sessionStorage.clear();
  window.location.href = 'user-portal.html';
});

document.addEventListener('DOMContentLoaded', () => showSection('myassets'));
