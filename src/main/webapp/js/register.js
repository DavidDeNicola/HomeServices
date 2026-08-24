const togglePassword = document.querySelector('#togglePassword');
const password = document.querySelector('#pass');
const eyeIcon = document.querySelector('#eyeIcon');
const confirmPassword = document.querySelector('#confirmPasswordInput');
const matchMessage = document.querySelector('#passwordMatchMessage');
const subBtn = document.querySelector('button[type="submit"]');

togglePassword.addEventListener('click', function () {
	const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
    password.setAttribute('type', type);
	confirmPassword.setAttribute('type', type);

    eyeIcon.classList.toggle('bi-eye');
    eyeIcon.classList.toggle('bi-eye-slash');

});

function checkPass() {
	const pass1= password.value;
	const pass2= confirmPassword.value;

	subBtn.classList.remove('btn-danger', 'btn-login','btn-success');

	if(pass2.length > 0) {
		matchMessage.style.display = 'block';
		if(pass1 === pass2){
			matchMessage.textContent = 'Le password corrispondono';
			matchMessage.className = 'small mt-1 text-success';
					
			subBtn.disabled = false;
		} else {
			matchMessage.textContent = 'Le password non corrispondono';
			matchMessage.className = 'small mt-1 text-danger';
					
			subBtn.disabled = true;
			subBtn.classList.add('btn-danger');	
		}
	} else {
		matchMessage.style.display = 'none';
		subBtn.disabled = false;
		subBtn.classList.add('btn-login');
	}
}

		password.addEventListener('input', checkPass);
		confirmPassword.addEventListener('input', checkPass);