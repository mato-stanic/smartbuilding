var confirmCallback = function(){
    alert("Confirm callback function not implemented!");
}

function showConfirmDialog(onConfirmFn, title, message, okBtnText, cancelBtnText){

    confirmCallback = onConfirmFn;

    $("#confirmDeleteModal .modal-title").html(title ? title : "Potvrdi");
    $("#confirmDeleteModal .modal-body").html(message ? message : "Jeste li sigurni da želite izvršiti ovu akciju?");
    $("#confirmDeleteModal .ok-btn").html(okBtnText ? okBtnText : "Potvrdi");
    $("#confirmDeleteModal .cancel-btn").html(cancelBtnText ? cancelBtnText : "Otkaži");

    if(title)
        $("#confirmDeleteModal .modal-header").show();
    else
        $("#confirmDeleteModal .modal-header").hide();


    $("#confirmDeleteModal").modal("show");
}

function hideConfirmDialog(){
    $("#confirmDeleteModal").modal("hide");
}

function showMessageDialog(title, message){
    $("#messageModal .modal-title").html(title ? title : "Poruka");
    $("#messageModal .modal-body").html(message ? message : "");
    if(title)
        $("#messageModal .modal-header").show();
    else
        $("#messageModal .modal-header").hide();

    $("#messageModal").modal("show");
}

function hideConfirmDialog(){
    $("#messageModal").modal("hide");
}