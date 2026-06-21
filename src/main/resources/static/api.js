const API = 'http://localhost:8085';
//Auth helpers
function getToken() { return sessionStorage.getItem('token'); }
function getUsername() { return sessionStorage.getItem('username') || 'Admin'; }
function authHeaders() {
  return {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${getToken()}`
  };
}
//Guard: redirect to login if not authed
if (!getToken()) {
  window.location.href = 'index.html';
}
//Set usernames
document.getElementById('sidebarUsername').textContent = getUsername();
document.getElementById('topbarUsername').textContent = getUsername();
//Toast
const toastEl = document.getElementById('appToast');
const bsToast = new bootstrap.Toast(toastEl, { delay: 3200 });
function showToast(msg, type = 'success') {
  document.getElementById('toastMsg').textContent = msg;
  toastEl.className = 'toast align-items-center border-0 toast-' + type;
  bsToast.show();
}
//API fetch wrapper
async function api(path, options = {}) {
  const res = await fetch(`${API}${path}`, {
    ...options,
    headers: { ...authHeaders(), ...(options.headers || {}) }
  });
  if (res.status === 401 || res.status === 403) {
    sessionStorage.clear();
    window.location.href = 'index.html';
    return;
  }
  if (!res.ok) {
    let errMsg = `Error ${res.status}`;
    try {
      const d = await res.json();
      if (d.message) {
        errMsg = d.message;
      } else if (typeof d === 'object') {
        const msgs = Object.values(d).filter(v => typeof v === 'string');
        if (msgs.length > 0) errMsg = msgs.join(', ');
      }
    } catch(e) {}
    throw new Error(errMsg);
  }
  const ct = res.headers.get('content-type') || '';
  if (ct.includes('application/json')) return res.json();
  return res.text();
}
// ROUTING / PAGE NAVIGATION
const pages = {
  dashboard:   { title: 'Dashboard',        breadcrumb: 'Overview of your asset portfolio' },
  assets:      { title: 'Assets',           breadcrumb: 'Manage all your hardware and software assets' },
  categories:  { title: 'Categories',       breadcrumb: 'Organize assets into categories' },
  employees:   { title: 'Employees',        breadcrumb: 'Manage employee directory' },
  assignments: { title: 'Asset Assignments',breadcrumb: 'Track asset allocations to employees' },
  maintenance: { title: 'Maintenance Logs', breadcrumb: 'Track service and repair history' },
};
let currentPage = 'dashboard';
function navigateTo(page, action) {
  if (!pages[page]) return;
  // Update active nav
  document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
  const navEl = document.getElementById(`nav-${page}`);
  if (navEl) navEl.classList.add('active');
  // Update page title/breadcrumb
  document.getElementById('pageTitle').textContent = pages[page].title;
  document.getElementById('pageBreadcrumb').textContent = pages[page].breadcrumb;
  // Show/hide sections
  document.querySelectorAll('.page-section').forEach(el => el.classList.add('d-none'));
  const section = document.getElementById(`page-${page}`);
  if (section) section.classList.remove('d-none');
  currentPage = page;
  // Load page data
  switch (page) {
    case 'dashboard':   loadDashboard(); break;
    case 'assets':      loadAssets(); break;
    case 'categories':  loadCategories(); break;
    case 'employees':   loadEmployees(); break;
    case 'assignments': loadAssignments(); break;
    case 'maintenance': loadMaintenance(); break;
  }
  // Trigger add if action=add
  if (action === 'add') {
    setTimeout(() => {
      const btnMap = {
        assets: 'btnAddAsset', categories: 'btnAddCategory',
        employees: 'btnAddEmployee', assignments: 'btnAddAssignment',
        maintenance: 'btnAddMaintenance'
      };
      const btn = document.getElementById(btnMap[page]);
      if (btn) btn.click();
    }, 100);
  }
  // Close mobile sidebar
  closeSidebar();
}
// Sidebar click events
document.querySelectorAll('.nav-item[data-page]').forEach(el => {
  el.addEventListener('click', (e) => {
    e.preventDefault();
    navigateTo(el.dataset.page);
  });
});
// Quick action buttons
document.querySelectorAll('.quick-action-btn[data-page]').forEach(btn => {
  btn.addEventListener('click', () => {
    navigateTo(btn.dataset.page, btn.dataset.action);
  });
});
// Dashboard "view all" links
document.querySelectorAll('.btn-text-link[data-page]').forEach(btn => {
  btn.addEventListener('click', () => navigateTo(btn.dataset.page));
});
//Mobile sidebar
function openSidebar() {
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
//Refresh button
document.getElementById('refreshBtn').addEventListener('click', () => {
  const btn = document.getElementById('refreshBtn');
  btn.classList.add('spinning');
  navigateTo(currentPage);
  setTimeout(() => btn.classList.remove('spinning'), 900);
});
//Logout
document.getElementById('logoutBtn').addEventListener('click', () => {
  sessionStorage.clear();
  window.location.href = 'index.html';
});
// SEARCH / FILTER
function setupSearch(inputId, tableId) {
  const input = document.getElementById(inputId);
  if (!input) return;
  input.addEventListener('input', () => {
    const q = input.value.toLowerCase();
    const wrap = document.getElementById(tableId);
    if (!wrap) return;
    wrap.querySelectorAll('tbody tr').forEach(row => {
      row.style.display = row.textContent.toLowerCase().includes(q) ? '' : 'none';
    });
  });
}
// TABLE BUILDER
function buildTable(headers, rows) {
  if (!rows || rows.length === 0) {
    return `<div class="empty-state"><i class="bi bi-inbox"></i><p>No records found.</p></div>`;
  }
  const ths = headers.map(h => `<th>${h}</th>`).join('');
  const trs = rows.map(r => `<tr>${r}</tr>`).join('');
  return `
    <table class="data-table" role="table">
      <thead><tr>${ths}</tr></thead>
      <tbody>${trs}</tbody>
    </table>`;
}
function actionBtns(editFn, deleteFn) {
  return `
    <div class="d-flex gap-1">
      <button class="btn-action btn-action-edit" title="Edit" onclick="${editFn}">
        <i class="bi bi-pencil-fill"></i>
      </button>
      <button class="btn-action btn-action-delete" title="Delete" onclick="${deleteFn}">
        <i class="bi bi-trash3-fill"></i>
      </button>
    </div>`;
}
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
// DELETE
let deleteModal;
let pendingDelete = null;
function initDeleteModal() {
  if (!deleteModal) deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
}
function confirmDelete(type, id) {
  initDeleteModal();
  pendingDelete = { type, id };
  deleteModal.show();
}
document.getElementById('confirmDeleteBtn').addEventListener('click', async () => {
  if (!pendingDelete) return;
  const { type, id } = pendingDelete;
  const endpointMap = {
	asset: `/api/assets/${id}`,
	    category: `/api/categories/${id}`,
	    employee: `/api/employees/${id}`,
	    assignment: `/api/assignments/${id}`,
	    maintenance: `/api/maintenance/${id}`,
	  };
	  const loaderMap = {
	    asset: loadAssets, category: loadCategories,
	    employee: loadEmployees, assignment: loadAssignments,
	    maintenance: loadMaintenance,
	  };
	  try {
	    await api(endpointMap[type], { method: 'DELETE' });
	    showToast('Record deleted successfully.');
	    deleteModal.hide();
	    if (loaderMap[type]) loaderMap[type]();
	  } catch(e) {
	    showToast(e.message, 'error');
	  }
	  pendingDelete = null;
	});