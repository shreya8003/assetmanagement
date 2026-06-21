let maintenanceModal, maintenanceData = [];

function maintenanceStatusBadge(status) {
  const map = {
    'pending':     'badge-maintenance',
    'in progress': 'badge-assigned',
    'repaired':    'badge-available',
    'rejected':    'badge-retired',
  };
  const cls = map[(status || '').toLowerCase()] || 'badge-maintenance';
  return `<span class="badge-status ${cls}">${status || 'Pending'}</span>`;
}

function initMaintenanceModal() {
  if (!maintenanceModal) maintenanceModal = new bootstrap.Modal(document.getElementById('maintenanceModal'));
}

async function loadMaintenance() {
  document.getElementById('maintenanceTableWrap').innerHTML =
    `<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>`;
  try {
    maintenanceData = await api('/api/maintenance');
    const rows = maintenanceData.map(m => {
      const submittedBy = m.submittedByUsername
        ? `<small class="text-muted d-block">by ${m.submittedByUsername}</small>` : '';
      const rejMsg = m.rejectionMessage
        ? `<small class="text-danger d-block mt-1"><i class="bi bi-info-circle me-1"></i>${m.rejectionMessage}</small>` : '';
      return `
        <td class="col-primary">${m.asset ? m.asset.name : '—'}${submittedBy}</td>
        <td>${m.maintainanceDate || '—'}</td>
        <td>${m.description || '—'}${rejMsg}</td>
        <td>${maintenanceStatusBadge(m.status)}</td>
        <td>
          <div class="d-flex gap-1 flex-wrap">
            ${quickStatusBtns(m.id, m.status)}
            <button class="btn-action btn-action-edit" title="Edit" onclick="editMaintenance(${m.id})">
              <i class="bi bi-pencil-fill"></i>
            </button>
            <button class="btn-action btn-action-delete" title="Delete" onclick="confirmDelete('maintenance',${m.id})">
              <i class="bi bi-trash3-fill"></i>
            </button>
          </div>
        </td>
      `;
    });
    document.getElementById('maintenanceTableWrap').innerHTML =
      buildTable(['Asset', 'Date', 'Description', 'Status', 'Actions'], rows);
    setupSearch('maintenanceSearch', 'maintenanceTableWrap');
  } catch(e) {
    document.getElementById('maintenanceTableWrap').innerHTML =
      `<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>${e.message}</p></div>`;
  }
}

function quickStatusBtns(id, currentStatus) {
  const s = (currentStatus || '').toLowerCase();
  let btns = '';
  if (s === 'pending') {
    btns += `<button class="btn-action" style="background:rgba(59,130,246,0.15);color:#60a5fa" title="Mark In Progress"
      onclick="quickStatus(${id},'In Progress',null)"><i class="bi bi-hourglass-split"></i></button>`;
  }
  if (s === 'in progress') {
    btns += `<button class="btn-action" style="background:rgba(34,197,94,0.15);color:#4ade80" title="Mark Repaired"
      onclick="quickStatus(${id},'Repaired',null)"><i class="bi bi-check-circle-fill"></i></button>`;
  }
  if (s !== 'rejected' && s !== 'repaired') {
    btns += `<button class="btn-action" style="background:rgba(239,68,68,0.15);color:#f87171" title="Reject"
      onclick="openRejectModal(${id})"><i class="bi bi-x-circle-fill"></i></button>`;
  }
  return btns;
}

async function quickStatus(id, newStatus, rejectionMessage) {
  const m = maintenanceData.find(x => x.id === id);
  if (!m) return;
  try {
    await api(`/api/maintenance/${id}`, {
      method: 'PUT',
      body: JSON.stringify({
        asset: m.asset ? { id: m.asset.id } : null,
        maintainanceDate: m.maintainanceDate,
        description: m.description,
        status: newStatus,
        rejectionMessage: rejectionMessage || null
      })
    });
    showToast(`Marked as ${newStatus}`);
    loadMaintenance();
  } catch(e) {
    showToast(e.message, 'error');
  }
}

// Reject modal
let rejectTargetId = null;
let rejectModal;
function openRejectModal(id) {
  rejectTargetId = id;
  document.getElementById('rejectMessageInput').value = '';
  document.getElementById('rejectMessageInput').classList.remove('is-invalid');
  if (!rejectModal) rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
  rejectModal.show();
}
document.getElementById('confirmRejectBtn').addEventListener('click', async () => {
  const msg = document.getElementById('rejectMessageInput').value.trim();
  if (!msg) {
    document.getElementById('rejectMessageInput').classList.add('is-invalid');
    return;
  }
  await quickStatus(rejectTargetId, 'Rejected', msg);
  rejectModal.hide();
});

async function openMaintenanceModal(id) {
  initMaintenanceModal();
  const form = document.getElementById('maintenanceForm');
  form.classList.remove('was-validated');
  form.reset();
  document.getElementById('maintenanceStatus').value = 'Pending';
  document.getElementById('rejectionMessageWrap').classList.add('d-none');
  document.getElementById('maintenanceRejectionMessage').value = '';
  try {
    const assets = await api('/api/assets');
    document.getElementById('maintenanceAsset').innerHTML =
      '<option value="">Select asset…</option>' +
      assets.map(a => `<option value="${a.id}">${a.name}</option>`).join('');
  } catch(e) {}
  if (id) {
    const m = maintenanceData.find(x => x.id === id);
    if (!m) return;
    document.getElementById('maintenanceModalLabel').textContent = 'Edit Maintenance Log';
    document.getElementById('maintenanceId').value = m.id;
    if (m.asset) document.getElementById('maintenanceAsset').value = m.asset.id;
    document.getElementById('maintenanceDate').value = m.maintainanceDate || '';
    document.getElementById('maintenanceDescription').value = m.description || '';
    document.getElementById('maintenanceStatus').value = m.status || 'Pending';
    if ((m.status || '').toLowerCase() === 'rejected') {
      document.getElementById('rejectionMessageWrap').classList.remove('d-none');
      document.getElementById('maintenanceRejectionMessage').value = m.rejectionMessage || '';
    }
  } else {
    document.getElementById('maintenanceModalLabel').textContent = 'Log Maintenance';
    document.getElementById('maintenanceId').value = '';
  }
  maintenanceModal.show();
}

document.getElementById('maintenanceStatus').addEventListener('change', function() {
  const wrap = document.getElementById('rejectionMessageWrap');
  if (this.value === 'Rejected') {
    wrap.classList.remove('d-none');
  } else {
    wrap.classList.add('d-none');
    document.getElementById('maintenanceRejectionMessage').value = '';
  }
});

function editMaintenance(id) { openMaintenanceModal(id); }
document.getElementById('btnAddMaintenance').addEventListener('click', () => openMaintenanceModal(null));

document.getElementById('saveMaintenanceBtn').addEventListener('click', async () => {
  const status = document.getElementById('maintenanceStatus').value;
  const rejMsg = document.getElementById('maintenanceRejectionMessage').value.trim();
  const id = document.getElementById('maintenanceId').value;
  const assetId = document.getElementById('maintenanceAsset').value;
  const payload = {
    asset: { id: parseInt(assetId) },
    maintainanceDate: document.getElementById('maintenanceDate').value || null,
    description: document.getElementById('maintenanceDescription').value.trim(),
    status: status,
    rejectionMessage: status === 'Rejected' ? rejMsg : null,
  };
  try {
    if (id) {
      await api(`/api/maintenance/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Maintenance log updated!');
    } else {
      await api('/api/maintenance', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Maintenance log added!');
    }
    maintenanceModal.hide();
    loadMaintenance();
  } catch(e) {
    showToast(e.message, 'error');
  }
});
