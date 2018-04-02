window.onload = () => {
    document.getElementById("username").innerHTML = sessionStorage.getItem("username");
    document.getElementById("submitButton").addEventListener("click", () => {
        let firstName = document.getElementById("firstName").value;
        let lastName = document.getElementById("lastName").value;
        let password = document.getElementById("password").value;
        let email = document.getElementById("email").value;

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                presentInfo(data);
            }
        };
        xhr.open("POST", `update.do?firstName=${firstName}&lastName=${lastName}&password=${password}&email=${email}`);
        xhr.send();
    });
};

function presentInfo(data) {
    if(data.message === "SUBMISSION SUCCESSFUL") {
        document.getElementById("submitMessage").innerHTML = '<span class="label label-success label-center">Submit Success!</span>';
    }  
    else 
    {
        document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">Unable to submit</span>';
    }
};