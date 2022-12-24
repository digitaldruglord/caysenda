jQuery(function ($) {
    const provinceOnchange = (e)=>{
        const element = $(e.target);
        getAddressDataService({province:element.val()}).then((results)=>{
            $("#dictricts").empty();
            results.forEach((item)=>{
                $("#dictricts").append($(`<option value="${item.id}">${item.name}</option>`))
            })
            $("#dictricts").selectpicker('refresh');
        })
    }
    const dictrictOnchange = (e)=>{
        const element = $(e.target);
        getAddressDataService({dictrict:element.val()}).then((results)=>{
            $("#wards").empty();
            results.forEach((item)=>{
                $("#wards").append($(`<option value="${item.id}">${item.name}</option>`))
            })
            $("#wards").selectpicker('refresh');
        })
    }
    const changeAddressTab = (e)=>{
        if ($("#nav-address").hasClass("show")){
            $("#nav-address").removeClass("show")
            $('#nav-address-select').addClass('show')
        }else {
            $("#nav-address").addClass("show")
            $('#nav-address-select').removeClass('show')
        }

    }
    const submitAddress = (e)=>{
        const modal = $("#addressModal")
        let dataRequest = {};
        modal.find("input[field],select").each((index,target)=>{
            const element = $(target);
            dataRequest[element.attr("field")] = element.val();
        });
        if (dataRequest.ref){
            $(document.body).trigger('addloadding')
        }
        saveAddressService(dataRequest).then((results)=>{
            if (results.success){
                modal.modal("hide")
                if (results.redirectTo){
                    window.location.replace(results.redirectTo);
                }else {
                    window.location.reload();
                }
            }
            $(document.body).trigger('removeloadding')

        })
    }
    const getModal = (e,id)=>{
        if ($("#addressModal").length>0) $("#addressModal").remove();
        let dataRequest = {
            ref:"address"
        };
        if (id) dataRequest.id= id;
        $(document.body).trigger('addloadding');
        addressModalService(dataRequest).then((results)=>{
            $(document.body).trigger('removeloadding')
           if (results.success){
               const modal = $(results.message);
               $(document.body).append(modal);
               modal.modal("show")
               modal.find('select').selectpicker();
           }

        })
    }
    const modalAddressEdit = (e)=>{
        const id = $(e.target).data("id");
        getModal(e,id);
    }
    const onChangeSelectAddress = (e)=>{
        const btnElement = $(e.target);
        const wrapElement = btnElement.parent();
        const id = wrapElement.data("id");
        setDefaultAddressService(id).then((results)=>{
            const tabContentSelected = $("#nav-address")
            tabContentSelected.empty();
            wrapElement.clone().appendTo(tabContentSelected);
            tabContentSelected.find(".ajax-address-select").remove();
            changeAddressTab(e);
            $(document.body).trigger("address.onchange.address")
        })
    }
    const removeAddress = (e)=>{
        const id = $(e.target).data("id");
        removeAddressService(id).then((results)=>{
            window.location.reload();
        })
    }
    $(document.body).on('change',"#provinces",provinceOnchange);
    $(document.body).on('change',"#dictricts",dictrictOnchange);
    $(document.body).on('click',".change-address-tab",changeAddressTab);
    $(document.body).on('click','.ajax-submit-address',submitAddress);
    $(document.body).on('click','.ajax-address-modal',getModal);
    $(document.body).on('click','.ajax-address-edit',modalAddressEdit);
    $(document.body).on('click','.ajax-address-select',onChangeSelectAddress)
    $(document.body).on('click','.ajax-address-remove',removeAddress)
})
