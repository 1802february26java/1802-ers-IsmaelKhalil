window.onload = () => {
	//Redirect user to the right URL if he comes from somewhere else
	if(window.location.pathname !== '/ERS/login.do') {
		window.location.replace('login.do');
	}

	//Login Event Listener
	document.getElementById("login").addEventListener("click", () => {
		let username = document.getElementById("username").value;
		let password = document.getElementById("password").value;

		//AJAX Logic
		let xhr = new XMLHttpRequest();

		xhr.onreadystatechange = () => {
			//If the request is DONE (4), and everything is OK
			if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
				//Getting JSON from response body
				let data = JSON.parse(xhr.responseText);
				console.log(data);

				//Call login response processing
				login(data);
			}
		};

		//Doing a HTTP to a specific endpoint
		xhr.open("POST",`login.do?username=${username}&password=${password}`);

		//Sending our request
		xhr.send();
	});
}

function login(data) {
	//If message is a member of the JSON, it was AUTHENTICATION FAILED
	if(data.message) {
		document.getElementById("loginMessage").innerHTML = '<span class="label label-danger label-center">Wrong credentials.</span>';
	}
	else {
		//Using sessionStorage of JavaScript
        sessionStorage.setItem("id", data.id);
        sessionStorage.setItem("firstName", data.firstName);
        sessionStorage.setItem("lastName",data.lastName);
        sessionStorage.setItem("username", data.username);
        sessionStorage.setItem("password",data.password);
        sessionStorage.setItem("email",data.email);
        sessionStorage.setItem("employeeRole",JSON.stringify(data.employeeRole));
		
        let temp = sessionStorage.getItem('employeeRole');
        let viewEmployeeRole = JSON.parse(temp);
        console.log(viewEmployeeRole.id);
        console.log(viewEmployeeRole.type);

		if(viewEmployeeRole.id == 2)    {
			window.location.replace("leader-home.do");
		} else {
			window.location.replace("lackey-home.do");
		}
	}
}