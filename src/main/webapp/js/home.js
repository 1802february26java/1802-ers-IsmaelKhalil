window.onload = () => {
    document.getElementById("username").innerHTML = sessionStorage.getItem("employeeUsername");
}