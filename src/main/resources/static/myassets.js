async function loadMyAssets() {
  const wrap = document.getElementById('myAssetsTableWrap');
  if (!wrap) return;
  wrap.innerHTML = '<div class="loading-state"><div class="spinner-border text-primary" role="status"></div></div>';
  try {
    const assignments = await api('/api/assignments/my');
    if (!assignments || assignments.length === 0) {
      wrap.innerHTML = '<div class="empty-state"><i class="bi bi-inbox"></i><p>No assets assigned to you yet.</p></div>';
      return;
    }
    const rows = assignments.map(a => {
      const asset = a.asset || {};
      const assigned = a.assignedDate || '—';
      const returned = a.returnDate || '—';
      return `
        <td class="col-primary">${asset.name || '—'}</td>
        <td>${asset.serialno || '—'}</td>
        <td>${asset.category ? asset.category.name : '—'}</td>
        <td>${statusBadge(asset.status)}</td>
        <td>${assigned}</td>
        <td>${returned}</td>
      `;
    });
    wrap.innerHTML = buildTable(
      ['Asset Name', 'Serial No.', 'Category', 'Status', 'Assigned Date', 'Return Date'],
      rows
    );
    setupSearch('myAssetsSearch', 'myAssetsTableWrap');
  } catch (e) {
    wrap.innerHTML = '<div class="empty-state"><i class="bi bi-exclamation-circle"></i><p>Failed to load assets.</p></div>';
  }
}
