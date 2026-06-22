const API = window.location.origin;

if (sessionStorage.getItem('token') && sessionStorage.getItem('role') === 'USER') {
  window.location.href = 'user-dashboard.html';
}

// Toast
const toastEl  = document.getElementById('appToast');
const bsToast  = new bootstrap.Toast(toastEl, { delay: 3500 });
function showToast(msg, type = 'success') {
  document.getElementById('toastMsg').textContent = msg;
  toastEl.className = 'toast align-items-center border-0 toast-' + type;
  bsToast.show();
}

// Tab switching
document.getElementById('tabLogin').addEventListener('click', () => switchTab('login'));
document.getElementById('tabRegister').addEventListener('click', () => switchTab('register'));

function switchTab(tab) {
  const isLogin = tab === 'login';
  document.getElementById('loginForm').classList.toggle('d-none', !isLogin);
  document.getElementById('registerForm').classList.toggle('d-none', isLogin);
  document.getElementById('loginHint').classList.toggle('d-none', !isLogin);
  document.getElementById('registerHint').classList.toggle('d-none', isLogin);
  document.getElementById('tabLogin').classList.toggle('active', isLogin);
  document.getElementById('tabRegister').classList.toggle('active', !isLogin);
  document.getElementById('loginError').classList.add('d-none');
  document.getElementById('registerError').classList.add('d-none');
}

// Password toggle
document.getElementById('togglePassword').addEventListener('click', () => {
  const pw = document.getElementById('password');
  const icon = document.getElementById('togglePwIcon');
  pw.type = pw.type === 'password' ? 'text' : 'password';
  icon.className = pw.type === 'password' ? 'bi bi-eye' : 'bi bi-eye-slash';
});

// Login
document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;
  const errorEl  = document.getElementById('loginError');
  const btnText  = document.getElementById('loginBtnText');
  const btnSpin  = document.getElementById('loginBtnSpinner');

  errorEl.classList.add('d-none');
  btnText.classList.add('d-none');
  btnSpin.classList.remove('d-none');

  try {
    const res = await fetch(`${API}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    if (!res.ok) throw new Error('Incorrect credentials');
    const data = await res.json();

    if (data.role !== 'USER') {
      throw new Error('Incorrect credentials');
    }

    sessionStorage.setItem('token', data.token);
    sessionStorage.setItem('username', data.username || username);
    sessionStorage.setItem('role', data.role);

    document.querySelector('.login-card').style.opacity = '0';
    document.querySelector('.login-card').style.transform = 'translateY(-20px)';
    setTimeout(() => { window.location.href = 'user-dashboard.html'; }, 400);
  } catch (err) {
    document.getElementById('loginErrorMsg').textContent = err.message || 'Login failed.';
    errorEl.classList.remove('d-none');
    document.getElementById('username').classList.add('is-invalid');
    document.getElementById('password').classList.add('is-invalid');
  } finally {
    btnText.classList.remove('d-none');
    btnSpin.classList.add('d-none');
  }
});

['username', 'password'].forEach(id => {
  document.getElementById(id).addEventListener('input', () => {
    document.getElementById(id).classList.remove('is-invalid');
    document.getElementById('loginError').classList.add('d-none');
  });
});

// Register
document.getElementById('registerForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('regUsername').value.trim();
  const email    = document.getElementById('regEmail').value.trim();
  const password = document.getElementById('regPassword').value;
  const confirm  = document.getElementById('regConfirm').value;
  const errorEl  = document.getElementById('registerError');
  const btnText  = document.getElementById('registerBtnText');
  const btnSpin  = document.getElementById('registerBtnSpinner');

  errorEl.classList.add('d-none');

  if (password !== confirm) {
    document.getElementById('registerErrorMsg').textContent = 'Passwords do not match.';
    errorEl.classList.remove('d-none');
    return;
  }
  if (password.length < 6) {
    document.getElementById('registerErrorMsg').textContent = 'Password must be at least 6 characters.';
    errorEl.classList.remove('d-none');
    return;
  }

  btnText.classList.add('d-none');
  btnSpin.classList.remove('d-none');

  try {
    const res = await fetch(`${API}/api/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });
    const data = await res.json();
    if (!res.ok) {
      document.getElementById('registerErrorMsg').textContent = data.message || 'Registration failed.';
      errorEl.classList.remove('d-none');
      return;
    }
    showToast('Account created successfully! Please sign in.', 'success');
    document.getElementById('registerForm').reset();
    setTimeout(() => switchTab('login'), 1800);
  } catch (err) {
    document.getElementById('registerErrorMsg').textContent = 'Registration failed. Try again.';
    errorEl.classList.remove('d-none');
  } finally {
    btnText.classList.remove('d-none');
    btnSpin.classList.add('d-none');
  }
});
