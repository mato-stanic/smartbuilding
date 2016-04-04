var optionStatus = "";
var roleScope = "";
var optionType

function initEditTariffOption(userRoleScope, currentOptionStatus, currentOptionType){

    roleScope = userRoleScope;
    optionStatus = currentOptionStatus;
    optionType = currentOptionType;

    $("#expiryUnit").on("change", function(ev) {
        ev.preventDefault();
        var newValue = $(this).val();
        if (newValue == "DAY") {
            $(".time-limit-value").removeClass("hide");
        }
        else {
            $(".time-limit-value").addClass("hide");
        }
    });

    $("#optionType").on("change", function(ev) {
        ev.preventDefault();
        optionTypeChanged();
    });


    var validatorOptions = {excluded: [':disabled'], verbose:false};
    $('#editOptionForm').bootstrapValidator(validatorOptions);

    $("#reqApprovalBtn").on("click", function(ev){
        var optionId = $(this).attr("data-option-id");
        requestApproval(optionId);
    });

    $("#approveBtn").on("click", function(ev){
        var optionId = $(this).attr("data-option-id");
        approve(optionId);
    });

    $("#disapproveBtn").on("click", function(ev){
        var optionId = $(this).attr("data-option-id");
        disapprove(optionId);
    });

    $("#returnToEditBtn").on("click", function(ev){
        var packetId = $(this).attr("data-option-id");
        returnToEdit(packetId);
    });

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



    $("#tariffs").select2();

    updateButtons();
    optionTypeChanged();

    $(".price-operators").select2();


    setTimeout(setupToggleLimit, 3000);
    setupToggleLimit();



}

function optionTypeChanged(){
    var optiontype = $("#optionType").val();
    if(optiontype == 'DISCOUNT'){
        $("#option-type-discount").removeClass('hide');
        $("#option-type-traffic").addClass('hide');
    }
    else if(optiontype == 'TRAFFIC_AMOUNT'){
        $("#option-type-discount").addClass('hide');
        $("#option-type-traffic").removeClass('hide');
    }
}

function updateButtons(){

    //if (roleScope == 'EKIP' || optionStatus != "EDITING" ) {
    if (roleScope == 'ADMIN' ) { //temporarely disabled
        toggleFormElements("editOptionForm", true);

        $("#returnToEditBtn").prop("disabled", false);
        $("#deactivateBtn").prop("disabled", false);
        $("#reqApprovalBtn").prop("disabled", false);
        $("#approveBtn").prop("disabled", false);
        $("#disapproveBtn").prop("disabled", false);

        $("#content button:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn):not(.dropdown-toggle)").addClass("hide");
        $("#content .btn:not(#reqApprovalBtn):not(#approveBtn):not(#disapproveBtn):not(#deactivateBtn):not(#returnToEditBtn):not(.dropdown-toggle)").addClass("hide");
    }

    //prikazi akcije nad statusom tarife ovisno o trenutnom statusu
    if (optionStatus == "EDITING") {
        $("#status").text("U pripremi");
        $("#returnToEditBtn").addClass("hide");
        $("#reqApprovalBtn").removeClass("hide");
        $("#approveBtn").addClass("hide");
        $("#disapproveBtn").addClass("hide");
    }
    else if (optionStatus == "PENDING_APPROVAL") {
        $("#status").text("Čeka na odobrenje");
        $("#disapproveBtn").text("Odbij odobrenje");
        $("#returnToEditBtn").removeClass("hide");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (optionStatus == "APPROVED") {
        $("#status").text("Odobren");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").addClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").removeClass("hide");
    }
    else if (optionStatus == "DISAPPROVED") {
        $("#status").text("Odobrenje odbijeno");
        $("#disapproveBtn").text("Poništi odobrenje");
        $("#reqApprovalBtn").addClass("hide");
        $("#approveBtn").removeClass("hide");
        $("#returnToEditBtn").removeClass("hide");
        $("#disapproveBtn").addClass("hide");
    }


    if(roleScope == 'OPERATOR') {
        $("#saveBtn").prop("disabled", false);
        $("#saveBtn").removeClass('hide');
        $("#nextBtn").addClass("hide");
        $("#prevBtn").addClass("hide");
    }

}

function toggleLimit(inputGroup, limitToggle, unlimited, inputField, limitValueField) {
    var btnText = inputGroup.find(".btn span.txt");
    btnText.text(limitToggle.text());
    if (unlimited == 'true')
        inputField.addClass("hide");
    else
        inputField.removeClass("hide");

    limitValueField.val(unlimited);
}


function setupToggleLimit(){

    //$("a[href='#']").on("click", function(ev){
    //    ev.preventDefault();
    //});

    $(".limit-toggle").each(function() {

        var limitToggle = $(this);
        var inputGroup = limitToggle.closest(".input-group");
        var inputField = inputGroup.find("input[type=text]");
        var unlimited = limitToggle.attr("data-unlimited");
        var limitValueField = inputGroup.find("input[type=hidden]");

        limitToggle.on("click", function(ev){
            ev.preventDefault();
            toggleLimit(inputGroup, limitToggle, unlimited, inputField, limitValueField);
        });
    });


}

function showAddDestinationBenefitModal(btnEl) {

    var tableRow = $(btnEl).closest("tr");
    var benefitIndex = tableRow.index() - 1; //first row is header
    $("#addBenefitDestinationModal input[name='benefitIndex']").val(benefitIndex);

    if (benefitIndex >= 0 ){

        if(optionType == 'TRAFFIC_AMOUNT') {

            var callMinutes = parseInt(tableRow.find("input[name$='callMinutes']").val()) || 0;;
            var smsCount = parseInt(tableRow.find("input[name$='smsCount']").val()) || 0;
            var dataAmountMB = parseInt(tableRow.find("input[name$='dataAmountMB']").val()) || 0;
            var dataFullSpeedAmountMB = parseInt(tableRow.find("input[name$='dataFullSpeedAmountMB']").val()) || 0;

            $("#addBenefitDestinationModal input[name='callMinutes']").val(callMinutes);
            $("#addBenefitDestinationModal input[name='smsCount']").val(smsCount);
            //$("#addBenefitDestinationModal input[name='mmsCount']").val(mmsCount);
            $("#addBenefitDestinationModal input[name='dataAmountMB']").val(dataAmountMB);
            $("#addBenefitDestinationModal input[name='dataFullSpeedAmountMB']").val(dataFullSpeedAmountMB);

            $("#addBenefitDestinationModal input[name='freeCalls']").val(tableRow.find("input[name$='freeCalls']").val());
            $("#addBenefitDestinationModal input[name='freeSms']").val(tableRow.find("input[name$='freeSms']").val());
            //$("#addBenefitDestinationModal input[name='freeMms']").val(tableRow.find("input[name$='freeMms']").val());
            $("#addBenefitDestinationModal input[name='freeData']").val(tableRow.find("input[name$='freeData']").val());
            $("#addBenefitDestinationModal input[name='freeDataFullSpeed']").val(tableRow.find("input[name$='freeDataFullSpeed']").val());

            $("#addBenefitDestinationModal input[name='callsDiscountPercent']").val(0);
            $("#addBenefitDestinationModal input[name='smsDiscountPercent']").val(0);
            //$("#addBenefitDestinationModal input[name='mmsDiscountPercent']").val(0);
            $("#addBenefitDestinationModal input[name='dataDiscountPercent']").val(0);
        }
        else if(optionType == 'DISCOUNT'){

            var callsDiscountPercent = parseInt(tableRow.find("input[name$='callsDiscountPercent']").val()) || 0;;
            var smsDiscountPercent = parseInt(tableRow.find("input[name$='smsDiscountPercent']").val()) || 0;
            var dataDiscountPercent = parseInt(tableRow.find("input[name$='dataDiscountPercent']").val()) || 0;

            $("#addBenefitDestinationModal input[name='callsDiscountPercent']").val(callsDiscountPercent);
            $("#addBenefitDestinationModal input[name='smsDiscountPercent']").val(smsDiscountPercent);
            //$("#addBenefitDestinationModal input[name='mmsDiscountPercent']").val(tableRow.find("input[name$='mmsDiscountPercent']").val());
            $("#addBenefitDestinationModal input[name='dataDiscountPercent']").val(dataDiscountPercent);

            $("#addBenefitDestinationModal input[name='callMinutes']").val(0);
            $("#addBenefitDestinationModal input[name='smsCount']").val(0);
            //$("#addBenefitDestinationModal input[name='mmsCount']").val(0);
            $("#addBenefitDestinationModal input[name='dataAmountMB']").val(0);
            $("#addBenefitDestinationModal input[name='dataFullSpeedAmountMB']").val(0);

            $("#addBenefitDestinationModal input[name='freeCalls']").val(0);
            $("#addBenefitDestinationModal input[name='freeSms']").val(0);
            //$("#addBenefitDestinationModal input[name='freeMms']").val(0);
            $("#addBenefitDestinationModal input[name='freeData']").val(0);
            $("#addBenefitDestinationModal input[name='freeDataFullSpeed']").val(0);
        }
    }
    else{

            $("#addBenefitDestinationModal input[name='callMinutes']").val(0);
            $("#addBenefitDestinationModal input[name='smsCount']").val(0);
            //$("#addBenefitDestinationModal input[name='mmsCount']").val(0);
            $("#addBenefitDestinationModal input[name='dataAmountMB']").val(0);
            $("#addBenefitDestinationModal input[name='dataFullSpeedAmountMB']").val(0);

            $("#addBenefitDestinationModal input[name='freeCalls']").val(0);
            $("#addBenefitDestinationModal input[name='freeSms']").val(0);
            //$("#addBenefitDestinationModal input[name='freeMms']").val(0);
            $("#addBenefitDestinationModal input[name='freeData']").val(0);
            $("#addBenefitDestinationModal input[name='freeDataFullSpeed']").val(0);

            $("#addBenefitDestinationModal input[name='callsDiscountPercent']").val(0);
            $("#addBenefitDestinationModal input[name='smsDiscountPercent']").val(0);
            //$("#addBenefitDestinationModal input[name='mmsDiscountPercent']").val(0);
            $("#addBenefitDestinationModal input[name='dataDiscountPercent']").val(0);

    }

    $("#addBenefitDestinationModal").modal("show");

}


function addBenefitDestination() {

    var benefitIndex = parseInt($("#benefitForm input[name='benefitIndex']").val());
    var data = $("#benefitForm").serialize();
    saveBenefit(benefitIndex, data);

}

function addNewBenefit(){
    saveBenefit(-1, "benefitIndex=-1");
}

function saveBenefit(benefitIndex, parameters){

    var url = "admin/tariff-option/edit-benefit";

    $.ajax(url, {
        data : parameters,
        error: function () {
            $("#addBenefitDestinationModal").modal("hide");
            showMessageDialog("Greška", "Dodavanje benefita nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            console.log("textStatus: ", textStatus, "jqXHR: ", jqXHR)

            $("#addBenefitDestinationModal").modal("hide");

            $('#editOptionForm').data('bootstrapValidator').destroy();
            $('#editOptionForm').data('bootstrapValidator', null);

            if(benefitIndex >=0){
                var rowToReplace = $("#benefit-list table tr").eq(benefitIndex + 1); //+1 because first row is header
                console.log("rowToReplace: " , rowToReplace.prop("outerHTML"));
                rowToReplace.replaceWith("<tr>" + data + "</tr>");
            }

            else
                $('#benefit-list table tr:last').after("<tr>" + data + "</tr>");

            var validatorOptions = {excluded: [':disabled'], verbose:false};
            $('#editOptionForm').bootstrapValidator(validatorOptions);


            setupToggleLimit();

        }
    });
}



function removeBenefit(btnEl){

    var tableRow = $(btnEl).closest("tr");
    var benefitIndex = tableRow.index() - 1; //first row is header

    var url = "admin/tariff-option/remove-benefit?benefitIndex=" + benefitIndex;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje benefita nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                $('#editOptionForm').data('bootstrapValidator').destroy();
                $('#editOptionForm').data('bootstrapValidator', null);
                tableRow.remove();
                reIndexRows($("#benefit-list tr"), 1);
                var validatorOptions = {excluded: [':disabled'], verbose:false};
                $('#editOptionForm').bootstrapValidator(validatorOptions);
            }
            else {
                showMessageDialog("Greška", "Brisanje benefita nije uspjelo!")
            }
        }
    });
}


function removeDestination(btnEl){

    var tableRow = $(btnEl).closest("tr");
    var benefitIndex = tableRow.index() - 1; //first row is header
    var distDiv = $(btnEl).closest("div .row");
    var destIndex = distDiv.index(); //first row is header


    var url = "admin/tariff-option/remove-benefit?benefitIndex=" + benefitIndex + "&destIndex=" + destIndex;

    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Brisanje destinacije benefita nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                $('#editOptionForm').data('bootstrapValidator').destroy();
                $('#editOptionForm').data('bootstrapValidator', null);
                distDiv.remove();
                var validatorOptions = {excluded: [':disabled'], verbose:false};
                $('#editOptionForm').bootstrapValidator(validatorOptions);
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





function networkScopeChanged(selectEl) {

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


function requestApproval(optionId){

    var url = "admin/tariff-option/request-approval?id=" + optionId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Zahtjev za odobrenjem nije uspio!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                optionStatus = "PENDING_APPROVAL";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje je uspješno poslan!");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function approve(optionId){

    var url = "admin/tariff-option/approve?id=" + optionId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odobrenje paketa  nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                optionStatus = "APPROVED";
                updateButtons();
                showMessageDialog( null, "Tarifna opcija je odobrena");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}

function disapprove(optionId){

    var url = "admin/tariff-option/disapprove?id=" + optionId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Odbijanje zahtjeva za odobrenje tarifne opcije nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                optionStatus = "DISAPPROVED";
                updateButtons();
                showMessageDialog( null, "Zahtjev za odobrenje tarifne opcije je uspješno odbijen");
            }
            else {
                showMessageDialog("Greška", data);
            }
        }
    });
}

function returnToEdit(optionId){

    var url = "admin/tariff-option/return-to-edit?id=" + optionId ;
    $.ajax(url, {
        error: function () {
            showMessageDialog("Greška", "Vraćanje tarifne opcije na doradu nije uspjelo!");
        },
        success: function(data, textStatus, jqXHR){

            if(data == "SUCCESS") {
                optionStatus = "EDITING";
                updateButtons();
                showMessageDialog( null, "Tarifna opcija je uspješno vraćena na doradu");
            }
            else {
                showMessageDialog("Greška", data)
            }
        }
    });
}