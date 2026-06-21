let assetModal, assetsData = [];
function initAssetModal() {
  if (!assetModal) assetModal = new bootstrap.Modal(document.getElementById('assetModal'));
}
async function loadAssets() {
  document.getElementById('assetsTableWrap').innerHTML =
    `<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>`;
  try {
    assetsData = await api('/api/assets');
    renderAssetsTable();
    setupSearch('assetSearch', 'assetsTableWrap');
  } catch(e) {
    document.getElementById('assetsTableWrap').innerHTML =
      `<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>${e.message}</p></div>`;
  }
}
function renderAssetsTable() {
  const rows = assetsData.map(a => `
    <td class="col-primary">${a.name || '—'}</td>
    <td>${a.serialno || '—'}</td>
    <td>${a.category ? a.category.name : '—'}</td>
    <td>${statusBadge(a.status)}</td>
    <td>${a.purchaseDate || '—'}</td>
    <td>${a.purchasePrice != null ? '₹' + Number(a.purchasePrice).toLocaleString() : '—'}</td>
    <td>${actionBtns(`editAsset(${a.id})`, `confirmDelete('asset',${a.id})`)}</td>
  `);
  document.getElementById('assetsTableWrap').innerHTML =
    buildTable(['Name', 'Serial No.', 'Category', 'Status', 'Purchase Date', 'Price', 'Actions'], rows);
}
async function openAssetModal(id) {
  initAssetModal();
  const form = document.getElementById('assetForm');
  form.classList.remove('was-validated');
  form.reset();
  // Load categories into select
  try {
    const cats = await api('/api/categories');
    const sel = document.getElementById('assetCategory');
    sel.innerHTML = '<option value="">Select category…</option>' +
      cats.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
  } catch(e) {}
  if (id) {
    const a = assetsData.find(x => x.id === id);
    if (!a) return;
    document.getElementById('assetModalLabel').textContent = 'Edit Asset';
    document.getElementById('assetId').value = a.id;
    document.getElementById('assetName').value = a.name || '';
    document.getElementById('assetSerial').value = a.serialno || '';
    document.getElementById('assetStatus').value = a.status || '';
    document.getElementById('assetPrice').value = a.price || '';
    document.getElementById('assetPurchasePrice').value = a.purchasePrice || '';
    document.getElementById('assetPurchaseDate').value = a.purchaseDate || '';
    if (a.category) document.getElementById('assetCategory').value = a.category.id;
  } else {
    document.getElementById('assetModalLabel').textContent = 'Add Asset';
    document.getElementById('assetId').value = '';
  }
  assetModal.show();
}
function editAsset(id) { openAssetModal(id); }
document.getElementById('btnAddAsset').addEventListener('click', () => openAssetModal(null));
document.getElementById('saveAssetBtn').addEventListener('click', async () => {
  const form = document.getElementById('assetForm');
  const id = document.getElementById('assetId').value;
  const catId = document.getElementById('assetCategory').value;
  const payload = {
    name: document.getElementById('assetName').value.trim(),
    serialno: document.getElementById('assetSerial').value.trim(),
    status: document.getElementById('assetStatus').value,
    price: document.getElementById('assetPrice').value.trim(),
    purchasePrice: document.getElementById('assetPurchasePrice').value || null,
    purchaseDate: document.getElementById('assetPurchaseDate').value || null,
    category: catId ? { id: parseInt(catId) } : null,
  };
  try {
    if (id) {
      await api(`/api/assets/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Asset updated successfully!');
    } else {
      await api('/api/assets', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Asset added successfully!');
    }
    assetModal.hide();
    loadAssets();
  } catch(e) {
    showToast(e.message, 'error');
  }
});