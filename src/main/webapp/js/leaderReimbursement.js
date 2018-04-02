window.onload = () => {
    document.getElementById("username").innerHTML = sessionStorage.getItem("username");

    document.getElementById("getPending").addEventListener("click", () => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                presentReimbursements(data);
            }
        };
        xhr.open("GET", `pending.do?fetch=pending`)
        xhr.send();
    });

    document.getElementById("getResolved").addEventListener("click", () => {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                presentResolvedReimbursements(data);
            }
        };
        xhr.open("GET", `resolved.do?fetch=resolved`)
        xhr.send();
    });

    document.getElementById("getEmployeeReimbursements").addEventListener("click", () => {
        let employeeElement = document.getElementById('employee');
        let employee = employeeElement.options[employeeElement.selectedIndex].text;
        let id;
        if ( employee === "IZZY") {
            
            id = 61;
        } else if (employee === "Peter") {
            id = 1;
        } else {
            id = 0;
        }

        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                let data = JSON.parse(xhr.responseText);
                presentEmployeeReimbursements(data);
            }
        };
        xhr.open("GET", `specificEmployeeReimbursements.do?fetch=specific&id=${id}`)
        xhr.send();
    });
};

function presentReimbursements(data) {
    if(data.message) {
        document.getElementById("pendingMessage").innerHTML = '<span class="label label-danger label-center">Could not get List</span>';
    }  
    else 
    {
        let pendingList = document.getElementById("pendingList");
        pendingList.innerHTML = "";

        data.forEach((pending) => {
            let pendingNode = document.createElement("li");
            pendingNode.className = "list-group-item"; 
            let pendingNodeText = document.createTextNode(`Reimbursement ID: ${pending.id}, Amount: ${pending.amount}, Description: ${pending.description}, First name: ${pending.requester.firstName}, Last name: ${pending.requester.lastName}, Manager ID: ${pending.approver.id}, Status: ${pending.status.status}, Type: ${pending.type.type}`);
            pendingNode.appendChild(pendingNodeText);
            pendingList.appendChild(pendingNode);
        });
    }
};

function presentResolvedReimbursements(data) {
    if(data.message) {
        document.getElementById("resolvedMessage").innerHTML = '<span class="label label-danger label-center">Could not get List</span>';
    }  
    else 
    {
        let resolvedList = document.getElementById("resolvedList");
        resolvedList.innerHTML = "";

        data.forEach((resolved) => {
            let resolvedNode = document.createElement("li");
            resolvedNode.className = "list-group-item"; 
            let resolvedNodeText = document.createTextNode(`Reimbursement ID: ${resolved.id}, Amount: ${resolved.amount}, Description: ${resolved.description}, First name: ${resolved.requester.firstName}, Last name: ${resolved.requester.lastName}, Manager ID: ${resolved.approver.id}, Status: ${resolved.status.status}, Type: ${resolved.type.type}`);
            resolvedNode.appendChild(resolvedNodeText);
            resolvedList.appendChild(resolvedNode);
        });
    }
};

function presentEmployeeReimbursements(data) {
    if(data.message) {
        document.getElementById("specificMessage").innerHTML = '<span class="label label-danger label-center">Could not get List</span>';
    }  
    else 
    {
        let reimbursementList = document.getElementById("reimbursementList");
        reimbursementList.innerHTML = "";

        data.forEach((reimbursement) => {
            let reimbursementsNode = document.createElement("li");
            reimbursementsNode.className = "list-group-item"; 
            let reimbursementsNodeText = document.createTextNode(`Reimbursement ID: ${reimbursement.id}, Amount: ${reimbursement.amount}, Description: ${reimbursement.description}, First name: ${reimbursement.requester.firstName}, Last name: ${reimbursement.requester.lastName}, Manager ID: ${reimbursement.approver.id}, Status: ${reimbursement.status.status}, Type: ${reimbursement.type.type}`);
            reimbursementsNode.appendChild(reimbursementsNodeText);
            reimbursementList.appendChild(reimbursementsNode);
        });
    }
}