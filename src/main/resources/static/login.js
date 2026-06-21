const API = 'http://localhost:8085';

// Toggle password visibility
document.getElementById('togglePassword').addEventListener('click', () => {
  const pw = document.getElementById('password');
  const icon = document.getElementById('togglePwIcon');
  if (pw.type === 'password') {
    pw.type = 'text';
    icon.className = 'bi bi-eye-slash';
  } else {
    pw.type = 'password';
    icon.className = 'bi bi-eye';
  }
});

// Login form submit
document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;
  const errorEl = document.getElementById('loginError');
  const btnText = document.getElementById('loginBtnText');
  const btnSpinner = document.getElementById('loginBtnSpinner');
  errorEl.classList.add('d-none');
  btnText.classList.add('d-none');
  btnSpinner.classList.remove('d-none');
  try {
    const res = await fetch(`${API}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    if (!res.ok) throw new Error('Invalid credentials');
    const data = await res.json();
    if (data.role !== 'ADMIN') throw new Error('Incorrect credentials.');
    sessionStorage.setItem('token', data.token);
    sessionStorage.setItem('username', username);
    sessionStorage.setItem('role', data.role);
    // Animate out
    document.querySelector('.login-card').style.opacity = '0';
    document.querySelector('.login-card').style.transform = 'translateY(-20px)';
    setTimeout(() => { window.location.href = 'dashboard.html'; }, 400);
  } catch (err) {
    document.getElementById('loginErrorMsg').textContent = err.message || 'Login failed.';
    errorEl.classList.remove('d-none');
    document.getElementById('usernameGroup').querySelector('input').classList.add('is-invalid');
    document.getElementById('passwordGroup').querySelector('input').classList.add('is-invalid');
  } finally {
    btnText.classList.remove('d-none');
    btnSpinner.classList.add('d-none');
  }
});

// Remove invalid state on typing
['username', 'password'].forEach(id => {
  document.getElementById(id).addEventListener('input', () => {
    document.getElementById(id).classList.remove('is-invalid');
    document.getElementById('loginError').classList.add('d-none');
  });
});

// Redirect if already logged in
if (sessionStorage.getItem('token')) {
  window.location.href = 'dashboard.html';
}
