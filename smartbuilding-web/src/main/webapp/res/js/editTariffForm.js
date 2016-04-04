var wizzardMode = false;
var addPriceUrl = "admin/tariff/add-price";
var deactivateTariffUrl = "admin/tariff/deactivate";
var addFreeStuffUrl = "admin/tariff/add-free-stuff";
var removePriceUrl = "admin/tariff/remove-price";
var removeFreeStuffUrl = "admin/tariff/remove-free-stuff";
var returnToEditUrl = "admin/tariff/return-to-edit";
var requestApprovalUrl = "admin/tariff/request-approval";
var approveUrl = "admin/tariff/approve";
var disapproveUrl = "admin/tariff/disapprove";


function updateButtons(){

    //if (roleScope == 'EKIP' || tariffStatus != "EDITING") {

    if (roleScope == 'ADMIN' ) { //temporarly ,
        toggleFormElements("editTariffForm", true);

        $("#returnToEditBtn").prop("disabled", false);
        $("#deactivateBtn").prop("disabled", false);
        $("#reqApprovalBtn").prop("disabled", false);
        $("#approveBtn").prop("disabled", false);
        $("#disapproveBtn").prop("disabled", false);


        $("#content button:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn)").addClass("hide");
        $("#content .btn:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn)").addClass("hide");

    }


    //prikazi akcije nad statusom tarife ovisno o trenutnom statusu
    if (tariffStatus == "EDITING") {
        $("#status").text("U pripremi");
        $("#returnToEditBtn").addClass("hide");
        $("#reqApprovalBtn").removeClass("hide");
        $("#approveBtn").addClass("hide");
        $("#disapproveBtn").addClass("hide");
    }
    else if (tariffStatus == "PENDING_APPROVAL") {
        $("#status").text("Čeka na odobrenje");
        $("#disapproveBtn").text("Odbij odobrenje");
        $("#returnToEditBtn").removeClass("hide");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (tariffStatus == "APPROVED") {
        $("#status").text("Odobrena");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").addClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (tariffStatus == "DISAPPROVED") {
        $("#status").text("Odobrenje odbijeno");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").addClass("hide");
    }


    if(roleScope == 'OPERATOR') {
        $("#saveBtn").prop("disabled", false);

        $("#tabOther input").prop("disabled", false);
        $("#tabOther textarea").prop("disabled", false);

        if (!wizzardMode) {
            $("#saveBtn").removeClass('hide');
            $("#deactivateBtn").removeClass('hide');
            $("#nextBtn").addClass("hide");
            $("#prevBtn").addClass("hide");
            return;
        }


        var firstSelected = $('#tabs li.active').is(':first-child');
        var lastSelected = $('#tabs li.active').is(':last-child');

        if (lastSelected) {
            $("#saveBtn").removeClass("hide");
            $("#nextBtn").addClass("hide");
            $('#editTariffForm').data('bootstrapValidator').disableSubmitButtons(false);
        }
        else {
            $("#saveBtn").addClass("hide");
            $("#nextBtn").removeClass("hide");
        }

        if (firstSelected)
            $("#prevBtn").addClass("hide");
        else
            $("#prevBtn").removeClass("hide");
    }

}

function gotoNextTab(){

    var currentTabSelector = $('#tabs li.active').find("a").attr("href");
    $('#editTariffForm').data('bootstrapValidator').validate();
    var tabValid = $('#editTariffForm').data('bootstrapValidator').isValidContainer(currentTabSelector);
    if(!tabValid){
        return;
    }

    var nextTab =  $('#tabs li.active').next().find("a");
    if(nextTab){
        nextTab.tab('show');
    }
    updateButtons();

}

function gotoPrevTab(){
    var prevTab =  $('#tabs li.active').prev().find("a");
    if(prevTab){
        prevTab.tab('show');
    }
    updateButtons();
}


function setupValidation(wizzardMode) {
    var validatorOptions = wizzardMode ? {excluded: [':disabled', '.tab-pane:not(.active) :hidden'], verbose:false} : {excluded: [':disabled'], verbose:false};
    $('#editTariffForm').bootstrapValidator(validatorOptions);
}

function setupPricesChangeListeners() {
    $(".network-scope-select").each(function (index) {
        networkScopeChanged(this);
        $(this).on("change", function (ev) {
            networkScopeChanged(this);
        });
    });
    $(".zone-select").each(function (index) {
        priceZoneChanged(this);
        $(this).on("change", function (ev) {
            priceZoneChanged(this);
        });
    });
}


var roleScope = '';
var tariffStatus = '';

function initTarrifForm(wizzard, userRoleScope, currentTariffStatus){

    roleScope = userRoleScope;
    tariffStatus = currentTariffStatus;

    $("#addPriceModal select[name='operators']").select2();
    $("#addFreeStuffModal select[name='operators']").select2();

    wizzardMode = wizzard;

    updateButtons();
    setupPricesChangeListeners();
    setupValidation(wizzardMode);

    $(window).keydown(function(event){
        if(event.keyCode == 13) {
            event.preventDefault();
            return false;
        }
    });

    $(".add-price").on("click", function(ev){
        ev.preventDefault();
        var priceType = $(this).attr("data-price-type");
        var userType = $("#tariffUserType").val();
        showPriceModal(priceType, userType);
    });



    $("#reqApprovalBtn").on("click", function(ev){
       var tariffId = $(this).attr("data-tariff-id");
       var activeFromSet = $(this).attr("data-active-from-set");
       var activeFrom = $("#activeFrom").val();

        if(activeFromSet == 'true' && activeFrom)
            requestApproval(tariffId);
        else
            showMessageDialog(null, "Odobrenje se ne može zatražiti dok nije zadan i spremljen datum od kada tarifa vrijedi!");

    });

    $("#approveBtn").on("click", function(ev){
        var tariffId = $(this).attr("data-tariff-id");
        approve(tariffId);
    });

    $("#disapproveBtn").on("click", function(ev){
        var tariffId = $(this).attr("data-tariff-id");
        disapprove(tariffId);
    });

    $("#returnToEditBtn").on("click", function(ev){
        var tariffId = $(this).attr("data-tariff-id");
        returnToEdit(tariffId);
    });

    $(".wizzard-nav a").on("click", function(ev){
        ev.preventDefault();
    });

    $("#submitPrice").on("click", function(ev){
        var type = $("#addPriceModal input[name='priceType']").val();
        addPrice(type);
    });

    $("#submitFreeMinutes").on("click", function(ev){
        addFreeStuff();
    });

    userTypeChanged();

}

function showPriceModal(type, userType) {

    if(type == 'call' || type == 'fn') {
        $("#addPriceModal .amount2-group").removeClass("hide");
    }
    else {
        $("#addPriceModal .amount2-group").addClass("hide");
        $("#addPriceModal input[name='amount2']").val(0);
    }

    if(type =='fn' && userType == 'LEGAL_PERSON'){

        var nationalZoneId = $("#addPriceModal select[name='targetZone'] option[data-zone-code='MONTENEGRO']").val();
        $("#addPriceModal select[name='targetZone']").val(nationalZoneId);
        $("#addPriceModal select[name='targetZone']").prop("disabled", true);

        $("#addPriceModal select[name='networkScope']").val("SPECIFIC_OPERATOR");
        networkScopeChanged($("#addPriceModal select[name='networkScope']"));
        $("#addPriceModal select[name='networkScope']").prop("disabled", true);

        $("#addPriceModal .price-operators option[data-own-network='false']").addClass('hide');
        $("#addPriceModal select.price-operators").select2();
    }
    else {
        $("#addPriceModal .price-operators option").removeClass('hide');
        $("#addPriceModal select.price-operators").select2();
        $("#addPriceModal select[name='networkScope']").prop("disabled", false);
        $("#addPriceModal select[name='targetZone']").prop("disabled", false);
    }

    $("#addPriceModal input[name='priceType']").val(type);
    $("#addPriceModal").modal("show");
}

function showFreeStuffModal(fsIndex){

    $("#freeStuffForm input[name='index']").val(fsIndex >= 0 ? fsIndex : -1);

    if(fsIndex >= 0){
        $("#addFreeStuffModal .modal-title").text("Dodaj destinaciju benefita");
        $("#addFreeStuffModal input[name='minutes']").closest(".form-group").addClass("hide");
        $("#addFreeStuffModal input[name='smsCount']").closest(".form-group").addClass("hide");
        //$("#addFreeStuffModal input[name='mmsCount']").closest(".form-group").addClass("hide");
        //$("#addFreeStuffModal input[name='dataAmountMB']").closest(".form-group").addClass("hide");
    }
    else {
        $("#addFreeStuffModal .modal-title").text("Dodaj benfit");
        $("#addFreeStuffModal input[name='minutes']").closest(".form-group").removeClass("hide");
        $("#addFreeStuffModal input[name='smsCount']").closest(".form-group").removeClass("hide");
        //$("#addFreeStuffModal input[name='mmsCount']").closest(".form-group").removeClass("hide");
        //$("#addFreeStuffModal input[name='dataAmountMB']").closest(".form-group").removeClass("hide");
    }

    $("#addFreeStuffModal").modal("show");
}


function showDeactivateTariffModal(tariffId, tariffName){

    $("#deactivateModal .deactivate-tariff").removeClass("hide");
    $("#deactivateModal .deactivate-packet").addClass("hide");
    $("#deactivateModal .modal-title").text("Arhiviranje tarife: " + tariffName);
    $("#deactivateModal input[name='id']").val(tariffId);
    $("#deactivateModal").modal("show");
}

function deactivateTariff (operatorId) {
    var data = $("#deactivateForm").serialize();
    var url = deactivateTariffUrl;
    $.ajax(url, {
        data : data,
        error: function () {
            $("#deactivateModal").modal("hide");
            showMessageDialog("Greška", "Arhiviranje tarife nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){
            $("#deactivateModal").modal("hide");
            window.location = "admin/tariff/list?operatorId=" + operatorId;
        }
    });
}

function setPriceTableVisibility(priceListDiv) {
    var rowCount = $(priceListDiv + ' table tr').length;
    if (rowCount > 1)
        $(priceListDiv + ' table').removeClass("hide");
    else
        $(priceListDiv + ' table').addClass("hide");
}

function addPrice(type) {

    var url = addPriceUrl + "/" + type;
    $("#priceForm select").prop("disabled", false);

    var data = $("#priceForm").serialize();
    $.ajax(url, {
        data : data,
        error: function () {
            $("#addPriceModal").modal("hide");
            showMessageDialog("Greška", "Dodavanje cijene nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            $("#addPriceModal").modal("hide");

            $('#editTariffForm').data('bootstrapValidator').destroy();
            $('#editTariffForm').data('bootstrapValidator', null);

            var targetDiv = "#price-list-" + type;
            $(targetDiv + ' table tr:last').after("<tr>" + data + "</tr>");

            setPriceTableVisibility(targetDiv);
            var validatorOptions = wizzardMode ? {excluded: [':disabled', '.tab-pane:not(.active) :hidden'], verbose:false} : {excluded: [':disabled'], verbose:false};
            $('#editTariffForm').bootstrapValidator(validatorOptions);
        }
    });
}


function addFreeStuff(){

    var fsIndex = parseInt($("#freeStuffForm input[name='index']").val());
    var url = addFreeStuffUrl;
    var data = $("#freeStuffForm").serialize();

    console.log("add free stuff data:", data);

    $.ajax(url, {
        data : data,
        error: function () {
            $("#addFreeStuffModal").modal("hide");
            showMessageDialog("Greška", "Dodavanje besplatnih minuta nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            console.log("status: " + data);

            $("#addFreeStuffModal").modal("hide");
            $('#editTariffForm').data('bootstrapValidator').destroy();
            $('#editTariffForm').data('bootstrapValidator', null);

            if(fsIndex >=0){
                var rowToReplace = $("#free-stuff-list table tr").eq(fsIndex + 1); //+1 because first row is header
                console.log("rowToReplace: " , rowToReplace.prop("outerHTML"));
                rowToReplace.replaceWith("<tr>" + data + "</tr>");
            }

            else
                $('#free-stuff-list table tr:last').after("<tr>" + data + "</tr>");

            var validatorOptions = wizzardMode ? {excluded: [':disabled', '.tab-pane:not(.active) :hidden'], verbose:false} : {excluded: [':disabled'], verbose:false};
            $('#editTariffForm').bootstrapValidator(validatorOptions);

        }
    });
}

function removeValidation() {
    $('#editTariffForm').data('bootstrapValidator').destroy();
    $('#editTariffForm').data('bootstrapValidator', null);
}

function removePrice(btnEl){

    var type = $(btnEl).attr("data-price-type");
    var tableRow = $(btnEl).parent().parent("tr");
    var index = tableRow.index() - 1; //first row is header

    var url = removePriceUrl + "/" + type + "?index=" + index;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje cijene nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                removeValidation();
                tableRow.remove();
                reIndexRows($("#price-list-" + type + " tr"), 1);
                var priceListDiv = "#price-list-" + type;
                setPriceTableVisibility(priceListDiv);
                setupValidation(wizzardMode)
            }
            else {
                showMessageDialog("Greška", "Brisanje cijene nije uspjelo!")
            }
        }
    });
}

function removeFreeStuff(btnEl){

    var tableRow = $(btnEl).closest("tr");
    var index = tableRow.index() - 1; //first row is header

    var url = removeFreeStuffUrl + "?index=" + index;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje benefita nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                removeValidation();
                tableRow.remove();
                reIndexRows($("#free-stuff-list tr"), 1);
                setupValidation(wizzardMode);
            }
            else {
                showMessageDialog("Greška", "Brisanje benefita nije uspjelo!")
            }
        }
    });
}

function removeFreeStuffDest(btnEl){

    var tableRow = $(btnEl).closest("tr");
    var index = tableRow.index() - 1; //first row is header

    var distDiv = $(btnEl).closest("div .row");
    var distIndex = distDiv.index(); //first row is header

    var url = removeFreeStuffUrl + "?index=" + index + "&destIndex=" + distIndex;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje destinacije benefita nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                removeValidation();
                distDiv.remove();
                setupValidation(wizzardMode);
            }
            else {
                showMessageDialog("Greška", "Brisanje destinacije benefita nije uspjelo!")
            }
        }
    });
}

function reIndexRows(rows, start){
    rows.each(function() {
        var row = $(this);
        var rowIndex = row.index() - start;
        var inputs = row.find("input, select");
        inputs.each(function(){
            var inp = $(this);
            var nameAttr = inp.attr("name");
            if(nameAttr.search(/\[(.*)\]/) > -1) {
                var newNameAttr = nameAttr.replace(/\[(.*)\]/, "[" + rowIndex + "]");
                inp.attr("name", newNameAttr);
            }
        });
    });
}



function addFriendNumber(){

    var url = addFriendNoUrl;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Dodavanje f&f broja nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){
            removeValidation();
            var targetDiv = "#friend-number-list";
            $(targetDiv + ' table tr:last').after("<tr>" + data + "</tr>");

            //setupPricesChangeListeners();
            setupValidation(wizzardMode);
        }
    });
}

function networkScopeChanged(selectEl){

    var networkScopeEl = $(selectEl);
    var newValue= networkScopeEl.val();

    var networkTypeEl = networkScopeEl.closest("form").find("select[name='networkType']").closest(".form-group");
    var operatorsEl = networkScopeEl.closest("form").find("select[name='operators']").closest(".form-group");


    if(newValue == "SPECIFIC_OPERATOR"){
        networkTypeEl.addClass("hide");
        networkTypeEl.find("select").val("");
        operatorsEl.removeClass("hide");
    }
    else if(newValue == "HOME_NETWORK") {
        //networkTypeEl.attr("disabled", "disabled");
        networkTypeEl.addClass("hide");
        networkTypeEl.find("select").val("");
        operatorsEl.addClass("hide");
        operatorsEl.find("select").val([]);
    }
    else {
        networkTypeEl.removeClass("hide");
        operatorsEl.addClass("hide");
        //networkTypeEl.removeAttr("disabled");
    }
}


function priceZoneChanged(selectEl){

    var zoneEl = $(selectEl);
    var newValue = zoneEl.find("option[value='"+ zoneEl.val()+"']").text() ;

    var networkScopeEl = zoneEl.closest("form").find("select[name='networkScope']").closest(".form-group");
    var networkTypeEl = zoneEl.closest("form").find("select[name='networkType']").closest(".form-group");
    var operatorsEl = zoneEl.closest("form").find("select[name='operators']").closest(".form-group");

    if(newValue == "Lokalno" || newValue == "Crna Gora"){
        networkScopeEl.removeClass("hide");
        networkTypeEl.removeClass("hide");
        networkScopeChanged(networkScopeEl.find("select"));
    }
    else {
        networkTypeEl.removeClass("hide");
        networkScopeEl.addClass("hide");
        networkScopeEl.find("select").val("ALL_NETWORKS");
        operatorsEl.addClass("hide");
        //operatorsEl.find("select").val([]);
        operatorsEl.find("select").select2('data', null);
        networkScopeChanged(networkScopeEl.find("select"));
    }
}



function removeFriendNumber(btnEl){

    var tableRow = $(btnEl).parent().parent("tr");
    var index = tableRow.index() - 1; //first row is header

    var url = removeFriendNoUrl + "?index=" + index;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje f&f broja nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                removeValidation();
                tableRow.remove();
                reIndexRows($("#friend-number-list" + " tr"), 1);
                setupValidation(wizzardMode)
            }
            else {
                showMessageDialog("Greška", "Brisanje f&f broja nije uspjelo!")
            }
        }
    });
}

function requestApproval(tariffId){

    var url = requestApprovalUrl + "?id=" + tariffId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Zahtjev za odobrenjem nije uspio!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                tariffStatus = "PENDING_APPROVAL";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje je uspješno poslan!");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function approve(tariffId){

    var url = approveUrl + "?id=" + tariffId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odobrenje tarife  nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                tariffStatus = "APPROVED";
                updateButtons();
                showMessageDialog( null, "Tarifa je odobrena");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function disapprove(tariffId){

    var url = disapproveUrl + "?id=" + tariffId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odbijanje zahtjeva za odobrenje tarife nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                tariffStatus = "DISAPPROVED";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje tarife je uspješno odbijen");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}


function returnToEdit(tariffId){

    var url = returnToEditUrl + "?id=" + tariffId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Vraćanje tarife na doradu nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                tariffStatus = "EDITING";
                updateButtons();
                showMessageDialog( null, "Tarifa je uspješno vraćena na doradu");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function userTypeChanged(){
    var personType = $("#tariffUserType").val();
    if(personType == 'PHISICAL_PERSON'){
        $("#fnTitle").text("Najčešće pozivani brojevi");
    }
    else{
        $("#fnTitle").text("Grupe korisnika i pripadajuće cijene");
    }

}



