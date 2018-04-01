window.onload = () => {
    document.getElementById("username").innerHTML = sessionStorage.getItem("username");

    document.getElementById("submit").addEventListener("click", () => {
        let amount = document.getElementById('amount').value;
        let reimbursementType = typeElement.options[typeElement.selectedIndex].text;
        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                request(data);
            }
        };
        xhr.open("POST", `submit.do?amount=${amount}&reimbursementType=${reimbursementType}`);
        xhr.send();
    });
};

function request(data) {
    if(data.message === "SUBMISSION SUCCESSFUL") {
        document.getElementById("submitMessage").innerHTML = '<span class="label label-success label-center">Submit Success!</span>';
    }  
    else 
    {
        document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">Unable to submit</span>';
    }
};