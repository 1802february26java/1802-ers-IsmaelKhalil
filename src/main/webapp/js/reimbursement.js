window.onload = () =>{
    /** **/
    document.getElementById("loggedUsername").innerHTML = sessionStorage.getItem("username");



    //register event listener
    document.getElementById("submitButton").addEventListener("click", ()=>{
        let amount = document.getElementById("amount").value;
        if(amount <0){
            document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">NO NEGATIVE AMOUNT.</span>';
            setTimeout(() =>{ document.getElementById("submitMessage").innerHTML = '';}, 3000);
            return;
        }
        let description = document.getElementById("description").value;
        let reimbursementTypeName = document.getElementById("reimbursementTypeName").value;
        let reimbursementTypeId;
        if(reimbursementTypeName==='COURSE'){
            reimbursementTypeId = 2;
        }
        else if(reimbursementTypeName==='CERTIFICATION'){
            reimbursementTypeId = 3;
        }
        else if(reimbursementTypeName==='TRAVELING'){
            reimbursementTypeId = 4;
        }
        else{
            reimbursementTypeId = 1;
        }

        let formdata = new FormData();
        formdata.append('amount',amount);
        formdata.append('description',description);
        formdata.append('reimbursementTypeName',reimbursementTypeName);
        formdata.append('reimbursementTypeId',reimbursementTypeId);

        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status ===200){
                let data = JSON.parse(xhr.responseText);

                createReimbursement(data);
            }
        };

  
        xhr.open("POST",`submitRequest.do?amount=${amount}&description=${description}&reimbursementTypeId=${reimbursementTypeId}&reimbursementTypeName=${reimbursementTypeName}`);
       
        xhr.send(formdata);

    })
}

function disableAllComponents(){
    document.getElementById("amount").setAttribute("disabled","disabled");
    document.getElementById("description").setAttribute("disabled","disabled");
}

function createReimbursement(data) {
    
     disableAllComponents();
  
      if(data.message === "A REIMBURSEMENT HAS BEEN CREATED SUCCESSFULLY"){
        document.getElementById("submitMessage").innerHTML = '<span class="label label-success label-center">CREATED SUCCESSFULLY.</span>';
        
      setTimeout(() =>{ window.location.replace("home.do");}, 3000);
       
      }
      else{
        document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
           
      }
}
