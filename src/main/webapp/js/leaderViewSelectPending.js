window.onload = () =>{

    document.getElementById("username").innerHTML = sessionStorage.getItem("username");
    /** **/
    //Get event listener
    document.getElementById("getSelectedReimbursements").addEventListener("click", getSelectedReimbursements);
    //Get all pending reimbursement as soon as the page loads

    //filter
    document.getElementById("filter").addEventListener("keyup",filterTable);

    getSelectedReimbursements();
}

function getSelectedReimbursements(){
      //AJAX Logic
      let xhr = new XMLHttpRequest();

      xhr.onreadystatechange = () => {
          if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200){
              let data = JSON.parse(xhr.responseText);
                console.log(data);
 
              presentSelectedReimbursements(data);
          }
      };
      let selectedEmployeeId = sessionStorage.getItem("selectedEmployeeId");
         console.log(selectedEmployeeId);
        xhr.open("GET",`multipleRequests.do?fetch=viewSelectedList&selectedEmployeeId=${selectedEmployeeId}`);
 
   //Sending our request
   xhr.send();
}

function presentSelectedReimbursements(data) {
     
      if(data.message){
          document.getElementById("listMessage").innerHTML = '<span class="label label-danger label-center">Something went wrong.</span>';
      }
      else{

      let counter = 0; 
     let reimbursementList = document.getElementById("selectedReimbursementsList");
         reimbursementList.innerHTML="";
  data.forEach((reimbursement)=>{
         counter = counter + 1;    
         let tr = document.createElement('tr');   

         let td1 = document.createElement('td');
         let td2 = document.createElement('td');
         let td3 = document.createElement('td');
         let td4 = document.createElement('td');
         let td5 = document.createElement('td');
         let td6 = document.createElement('td');
         let td7 = document.createElement('td');
         let td8 = document.createElement('td');
         
         let text1 = document.createTextNode(`${reimbursement.id}`);
         let text2 = document.createTextNode(`${reimbursement.requested.year}-${reimbursement.requested.monthValue}-${reimbursement.requested.dayOfMonth}, ${reimbursement.requested.hour}:${reimbursement.requested.minute}:${reimbursement.requested.second}`);
         let text3;
         if(reimbursement.status.status === 'PENDING'){
             text3 = document.createTextNode(` `);
         }
         else{
             text3 = document.createTextNode(`${reimbursement.resolved.year}-${reimbursement.resolved.monthValue}-${reimbursement.resolved.dayOfMonth}, ${reimbursement.resolved.hour}:${reimbursement.resolved.minute}:${reimbursement.resolved.second}`);
         }
         let text4 = document.createTextNode(`${reimbursement.amount}`);
         let text5 = document.createTextNode(`${reimbursement.description}`);
         let text6 = document.createTextNode(`${reimbursement.requester.firstName} ${reimbursement.requester.lastName}`);
         let text7 = document.createTextNode(`${reimbursement.type.type}`);
         let text8 = document.createTextNode(`${reimbursement.status.status}`);

             td1.appendChild(text1);
             td2.appendChild(text2);
             td3.appendChild(text3);
             td4.appendChild(text4);
             td5.appendChild(text5);
             td6.appendChild(text6);
             td7.appendChild(text7);
             td8.appendChild(text8);

             tr.appendChild(td1);
             tr.appendChild(td2);
             tr.appendChild(td3);
             tr.appendChild(td4);
             tr.appendChild(td5);
             tr.appendChild(td6);
             tr.appendChild(td7);
             tr.appendChild(td8);
             reimbursementList.appendChild(tr);
          });
          document.getElementById("counter").innerHTML =counter;

      }
}


function filterTable(){

  let filter = document.getElementById("filter").value.toUpperCase();
  let table = document.getElementById("selectedReimbursementsList");
  let tr = table.getElementsByTagName("tr");
  let i, j;
  for (i = 0; i < tr.length; i++) {

    loop:  for(j = 0;j<8;j++){
        td = tr[i].getElementsByTagName("td")[j];
          if (td) {
             if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
                break loop;
           } else {
                tr[i].style.display = "none";
                
           }
        } 
        
      }
  
    }
}