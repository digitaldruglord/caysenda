
jQuery(function ($) {
    const updateTotal = (productElement,totalQuantity,totalPrice)=>{
        const totalElement = productElement.parents(".modal-body").find(".total")
        const quantityElement = totalElement.find(".quantity").find(".value");
        const priceElement = totalElement.find(".price").find(".value")
        priceElement.html(new Intl.NumberFormat().format(totalPrice)+'đ')
        quantityElement.html(totalQuantity)
    }
    const activeRangePrice = (productElement,range)=>{
        productElement.parents(".modal-body").find(".price-condition").find("ins").each((index,target)=>{
            const element = $(target);
            const rangeElement = element.data("range");
            if (rangeElement===range){
                if (!element.hasClass("font-weight-bold")){
                    element.addClass("font-weight-bold")
                }
            }else {
                element.removeClass("font-weight-bold")
            }
        })
    }
    const rsValue = (quickviewProduct)=>{
        quickviewProduct.find(".variant").each((index,target)=>{
            $(target).find(".js-result").val(0);
        })
    }
    $(document.body).on("onchange-input-quickview",(e,element)=>{
            const productElement = $(element).parents(".quickview-product");
            const variantElement = $(element).parents(".variant");
            const variantElements = productElement.find(".variant");
            if (productElement){
                const productData = productElement.data("product");
                const stock = variantElement.data("stock");
                const inputVariant = variantElement.find(".js-result");
                if (stock>=parseInt(inputVariant.val())){
                    if (productData.isRetailt){
                        let totalQuantity = 0;
                        let totalPrice = 0;
                        variantElements.each((index,variantTarget)=>{
                            const inputResult = $(variantTarget).find(".js-result").val();
                            const price = $(variantTarget).data("price")
                            totalQuantity+= parseInt(inputResult);
                            totalPrice+=price*parseInt(inputResult)
                        })
                        updateTotal(productElement,totalQuantity,totalPrice)
                    }else {
                        let totalQuantity = 0;
                        variantElements.each((index,variantTarget)=>{
                            const inputResult = $(variantTarget).find(".js-result").val();
                            totalQuantity+= parseInt(inputResult);
                        })
                        const rangeAndPrice = getRange(totalQuantity+productData.incart,productData);
                        activeRangePrice(productElement,rangeAndPrice.range)
                        updateTotal(productElement,totalQuantity,rangeAndPrice.price*totalQuantity)
                    }
                }else {
                    if (inputVariant.val()!==''){
                        inputVariant.val(stock);
                    }else {
                        inputVariant.val(0);
                    }
                }
            }
    })
    const getView = (productId)=>{

        $(document.body).trigger('addloadding')
        getQuickviewService(productId).then((results)=>{
            $(document.body).trigger('removeloadding')
            const modal = $(results.data);
            $(document.body).append(modal);
            modal.modal("show");
            $('.tooltip-element').tooltip();
            $.HSCore.components.HSQantityCounter.init('.js-quantity-quickview',{trigger:'onchange-input-quickview'});
            modal.on('hidden.bs.modal', function (e) {
                modal.remove();
            })
            $('.tooltip-element').on('show.bs.tooltip', function () {
                // do something…
                console.log("asdasd")
            })
        })
    }
    const openQuickview=(e)=>{
        e.preventDefault();
        const element = $(e.target);
        const productBody = element.parents('.product-item__body')
        const productId = productBody.find(".open-quickview").data("id")
        getView(productId);
    }
    const toast = (conditionPrice,status,message)=>{
        const messageToast = `<div class="quickview-toast m-0 alert ${status?'alert-success':'alert-danger'}" role="alert">
                            ${message}
                        </div>`
        const element = $(messageToast);
        $(element).insertAfter(conditionPrice);
        setTimeout(()=>{
            element.remove();
        },2000)
    }
    const addtocart = (e)=>{
        const modalBody = $(e.target).parents(".modal-content").find(".modal-body");
        const productElement = modalBody.find(".quickview-product");
        const variantElements = productElement.find(".variant");
        const conditionPrice = modalBody.find(".wrap-confition-price");
        const productData = productElement.data("product");
        let dataRequest = {
            productId:productData.id,
            detailts:[],
        }
        let totalQuantity = productData.incart;
        variantElements.each((index,variantTarget)=>{
            const inputResult = $(variantTarget).find(".js-result").val();
            const id = $(variantTarget).data("id");
            if (inputResult!=='' && parseInt(inputResult)>0){
                dataRequest.detailts.push({
                    quantity:parseInt(inputResult),
                    variantId:id
                })
                totalQuantity+=parseInt(inputResult)
            }
        })
        if (totalQuantity>=productData.conditiondefault){
            addtocartService(dataRequest).then((results)=>{
                if (results.success){
                    toast(conditionPrice,true,"Thêm vào giỏ hàng thành công");
                    updateWidget();
                    rsValue(productElement)
                    productElement.data("product",{...productData,incart:productData.incart+totalQuantity})
                }else {
                    if (results.code==='LOGIN'){
                        $(document.body).trigger("user.login");
                    }else {
                        toast(conditionPrice,false,"Thêm vào giỏ hàng thất bại");
                    }
                }
            })
        }else {
            toast(conditionPrice,false,"Tối thiểu "+productData.conditiondefault);
        }

    }

    $(document.body).on('click','.open-quickview',openQuickview)
    $(document.body).on('click','.quickview-add-to-cart',addtocart)
})