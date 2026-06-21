let categoryModal, categoriesData = [];
function initCategoryModal() {
  if (!categoryModal) categoryModal = new bootstrap.Modal(document.getElementById('categoryModal'));
}
async function loadCategories() {
  document.getElementById('categoriesTableWrap').innerHTML =
    `<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>`;
  try {
    categoriesData = await api('/api/categories');
    const rows = categoriesData.map(c => `
      <td class="col-primary">${c.name || '—'}</td>
      <td>${c.description || '—'}</td>
      <td>${actionBtns(`editCategory(${c.id})`, `confirmDelete('category',${c.id})`)}</td>
    `);
    document.getElementById('categoriesTableWrap').innerHTML =
      buildTable(['Name', 'Description', 'Actions'], rows);
    setupSearch('categorySearch', 'categoriesTableWrap');
  } catch(e) {
    document.getElementById('categoriesTableWrap').innerHTML =
      `<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>${e.message}</p></div>`;
  }
}
function openCategoryModal(id) {
  initCategoryModal();
  const form = document.getElementById('categoryForm');
  form.classList.remove('was-validated');
  form.reset();
  if (id) {
    const c = categoriesData.find(x => x.id === id);
    if (!c) return;
    document.getElementById('categoryModalLabel').textContent = 'Edit Category';
    document.getElementById('categoryId').value = c.id;
    document.getElementById('categoryName').value = c.name || '';
    document.getElementById('categoryDescription').value = c.description || '';
  } else {
    document.getElementById('categoryModalLabel').textContent = 'Add Category';
    document.getElementById('categoryId').value = '';
  }
  categoryModal.show();
}
function editCategory(id) { openCategoryModal(id); }
document.getElementById('btnAddCategory').addEventListener('click', () => openCategoryModal(null));
document.getElementById('saveCategoryBtn').addEventListener('click', async () => {
  const form = document.getElementById('categoryForm');
  const id = document.getElementById('categoryId').value;
  const payload = {
    name: document.getElementById('categoryName').value.trim(),
    description: document.getElementById('categoryDescription').value.trim(),
  };
  try {
    if (id) {
      await api(`/api/categories/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Category updated!');
    } else {
      await api('/api/categories', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Category added!');
    }
    categoryModal.hide();
    loadCategories();
  } catch(e) {
    showToast(e.message, 'error');
  }
});