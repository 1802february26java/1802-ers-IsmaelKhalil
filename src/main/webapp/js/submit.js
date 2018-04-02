window.onload = () =>{
    /** **/
    document.getElementById("username").innerHTML = sessionStorage.getItem("username");



    //register event listener
    document.getElementById("submitButton").addEventListener("click", ()=>{
        let amount = document.getElementById("amount").value;
        if(amount <0){
            document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">Invalid amount</span>';
            setTimeout(() =>{ document.getElementById("submitMessage").innerHTML = '';}, 3000);
            return;
        }
        let description = document.getElementById("description").value;
        let reimbursementType = document.getElementById("reimbursementType").value;
        let reimbursementTypeId;
        if(reimbursementType === 'COURSE'){
            reimbursementTypeId = 2;
        }
        else if(reimbursementType === 'CERTIFICATION'){
            reimbursementTypeId = 3;
        }
        else if(reimbursementType === 'TRAVELING'){
            reimbursementTypeId = 4;
        }
        else{
            reimbursementTypeId = 1;
        }

        let formdata = new FormData();
        formdata.append('amount',amount);
        formdata.append('description',description);
        formdata.append('reimbursementTypeId',reimbursementTypeId);
        formdata.append('reimbursementType',reimbursementType);

        let xhr = new XMLHttpRequest();

        xhr.onreadystatechange = () => {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200){
                let data = JSON.parse(xhr.responseText);

                submission(data);
            }
        };

  
        xhr.open("POST",`submit.do?amount=${amount}&description=${description}&reimbursementTypeId=${reimbursementTypeId}&reimbursementType=${reimbursementType}`);
       
        xhr.send(formdata);

    })
}

function disableAllComponents(){
    document.getElementById("amount").setAttribute("disabled","disabled");
    document.getElementById("description").setAttribute("disabled","disabled");
}

function submission(data) {
     disableAllComponents();
  
      if(data.message === "SUBMITTED REQUEST"){
        document.getElementById("submitMessage").innerHTML = '<span class="label label-success label-center">Successfully created</span>';
        
      setTimeout(() =>{ window.location.replace("lackey-home.do");}, 3000);
       
      }
      else{
        document.getElementById("submitMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
      }
}