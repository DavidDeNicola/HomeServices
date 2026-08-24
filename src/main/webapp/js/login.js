const togglePassword = document.querySelector('#togglePassword');
const password = document.querySelector('#pass');
const eye = document.querySelector('#eyeIcon');

togglePassword.addEventListener('click', function(e) {
	const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
	password.setAttribute('type', type);
			
	eye.classList.toggle('bi-eye');
	eye.classList.toggle('bi-eye-slash');
});