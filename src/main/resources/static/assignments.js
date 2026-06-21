let assignmentModal, assignmentsData = [];
function initAssignmentModal() {
  if (!assignmentModal) assignmentModal = new bootstrap.Modal(document.getElementById('assignmentModal'));
}
async function loadAssignments() {
  document.getElementById('assignmentsTableWrap').innerHTML =
    `<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>`;
  try {
    assignmentsData = await api('/api/assignments');
    const rows = assignmentsData.map(a => `
      <td class="col-primary">${a.asset ? a.asset.name : '—'}</td>
      <td>${a.employee ? a.employee.name : '—'}</td>
      <td>${a.employee ? a.employee.company || '—' : '—'}</td>
      <td>${a.assignedDate || '—'}</td>
      <td>${a.returnDate || '<span class="text-muted">Not returned</span>'}</td>
      <td>${actionBtns(`editAssignment(${a.id})`, `confirmDelete('assignment',${a.id})`)}</td>
    `);
    document.getElementById('assignmentsTableWrap').innerHTML =
      buildTable(['Asset', 'Employee', 'Company', 'Assigned Date', 'Return Date', 'Actions'], rows);
    setupSearch('assignmentSearch', 'assignmentsTableWrap');
  } catch(e) {
    document.getElementById('assignmentsTableWrap').innerHTML =
      `<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>${e.message}</p></div>`;
  }
}
async function openAssignmentModal(id) {
  initAssignmentModal();
  const form = document.getElementById('assignmentForm');
  form.classList.remove('was-validated');
  form.reset();
  // Load assets and employees
  try {
    const [assets, employees] = await Promise.all([api('/api/assets'), api('/api/employees')]);
    document.getElementById('assignmentAsset').innerHTML =
      '<option value="">Select asset…</option>' +
      assets.map(a => `<option value="${a.id}">${a.name} (${a.serialno || 'No SN'})</option>`).join('');
    document.getElementById('assignmentEmployee').innerHTML =
      '<option value="">Select employee…</option>' +
      employees.map(e => `<option value="${e.id}">${e.name} — ${e.company || '?'}</option>`).join('');
  } catch(e) {}
  if (id) {
    const a = assignmentsData.find(x => x.id === id);
    if (!a) return;
    document.getElementById('assignmentModalLabel').textContent = 'Edit Assignment';
    document.getElementById('assignmentId').value = a.id;
    if (a.asset) document.getElementById('assignmentAsset').value = a.asset.id;
    if (a.employee) document.getElementById('assignmentEmployee').value = a.employee.id;
    document.getElementById('assignedDate').value = a.assignedDate || '';
    document.getElementById('returnDate').value = a.returnDate || '';
  } else {
    document.getElementById('assignmentModalLabel').textContent = 'New Assignment';
    document.getElementById('assignmentId').value = '';
  }
  assignmentModal.show();
}
function editAssignment(id) { openAssignmentModal(id); }
document.getElementById('btnAddAssignment').addEventListener('click', () => openAssignmentModal(null));
document.getElementById('saveAssignmentBtn').addEventListener('click', async () => {
  const form = document.getElementById('assignmentForm');
  const id = document.getElementById('assignmentId').value;
  const assetId = document.getElementById('assignmentAsset').value;
  const empId = document.getElementById('assignmentEmployee').value;
  const payload = {
    asset: { id: parseInt(assetId) },
    employee: { id: parseInt(empId) },
    assignedDate: document.getElementById('assignedDate').value || null,
    returnDate: document.getElementById('returnDate').value || null,
  };
  try {
    if (id) {
      await api(`/api/assignments/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Assignment updated!');
    } else {
      await api('/api/assignments', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Assignment created!');
    }
    assignmentModal.hide();
    loadAssignments();
  } catch(e) {
    showToast(e.message, 'error');
  }
});