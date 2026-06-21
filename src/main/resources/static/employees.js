let employeeModal, employeesData = [];
function initEmployeeModal() {
  if (!employeeModal) employeeModal = new bootstrap.Modal(document.getElementById('employeeModal'));
}
async function loadEmployees() {
  document.getElementById('employeesTableWrap').innerHTML =
    `<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>`;
  try {
    employeesData = await api('/api/employees');
    const rows = employeesData.map(e => `
      <td class="col-primary">${e.name || '—'}</td>
      <td>${e.email || '—'}</td>
      <td>${e.company || '—'}</td>
      <td>${actionBtns(`editEmployee(${e.id})`, `confirmDelete('employee',${e.id})`)}</td>
    `);
    document.getElementById('employeesTableWrap').innerHTML =
      buildTable(['Name', 'Email', 'Company', 'Actions'], rows);
    setupSearch('employeeSearch', 'employeesTableWrap');
  } catch(e) {
    document.getElementById('employeesTableWrap').innerHTML =
      `<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>${e.message}</p></div>`;
  }
}
function openEmployeeModal(id) {
  initEmployeeModal();
  const form = document.getElementById('employeeForm');
  form.classList.remove('was-validated');
  form.reset();
  if (id) {
    const emp = employeesData.find(x => x.id === id);
    if (!emp) return;
    document.getElementById('employeeModalLabel').textContent = 'Edit Employee';
    document.getElementById('employeeId').value = emp.id;
    document.getElementById('employeeName').value = emp.name || '';
    document.getElementById('employeeEmail').value = emp.email || '';
    document.getElementById('employeeCompany').value = emp.company || '';
  } else {
    document.getElementById('employeeModalLabel').textContent = 'Add Employee';
    document.getElementById('employeeId').value = '';
  }
  employeeModal.show();
}
function editEmployee(id) { openEmployeeModal(id); }
document.getElementById('btnAddEmployee').addEventListener('click', () => openEmployeeModal(null));
document.getElementById('saveEmployeeBtn').addEventListener('click', async () => {
  const form = document.getElementById('employeeForm');
  const id = document.getElementById('employeeId').value;
  const payload = {
    name: document.getElementById('employeeName').value.trim(),
    email: document.getElementById('employeeEmail').value.trim(),
    company: document.getElementById('employeeCompany').value.trim(),
  };
  try {
    if (id) {
      await api(`/api/employees/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Employee updated!');
    } else {
      await api('/api/employees', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Employee added!');
    }
    employeeModal.hide();
    loadEmployees();
  } catch(e) {
    showToast(e.message, 'error');
  }
});