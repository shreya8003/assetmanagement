async function loadDashboard() {
  try {
    const [assets, employees, assignments, maintenance] = await Promise.all([
      api('/api/assets'),
      api('/api/employees'),
      api('/api/assignments'),
      api('/api/maintenance'),
    ]);
    animateCount('stat-assets', assets.length);
    animateCount('stat-employees', employees.length);
    animateCount('stat-assignments', assignments.length);
    animateCount('stat-maintenance', maintenance.length);
    // Recent assets table
    const recent = assets.slice(-5).reverse();
    const rows = recent.map(a => `
      <td class="col-primary">${a.name || '—'}</td>
      <td>${a.serialno || '—'}</td>
      <td>${a.category ? a.category.name : '—'}</td>
      <td>${statusBadge(a.status)}</td>
    `);
    const html = buildTable(['Name', 'Serial No.', 'Category', 'Status'], rows);
    document.getElementById('recentAssetsList').innerHTML = html;
  } catch(e) {
    console.error(e);
  }
}
function animateCount(id, target) {
  const el = document.getElementById(id);
  if (!el) return;
  let start = 0;
  const duration = 700;
  const step = Math.ceil(target / (duration / 16));
  const timer = setInterval(() => {
    start = Math.min(start + step, target);
    el.textContent = start;
    if (start >= target) clearInterval(timer);
  }, 16);
}