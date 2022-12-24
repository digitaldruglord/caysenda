const getRange = (quantity,data) => {
    if (quantity>=data.conditiondefault && quantity<data.condition1){
        return{range:1, price:data.vip1}
    }else if (quantity>=data.condition1 && quantity<data.condition2){
        return{range:2, price:data.vip2}
    }else if (quantity>=data.condition2){
        return{range:3, price:data.vip3}
    }else {
        return{range:0, price:data.vip1}
    }
}
const updateWidget = ()=>{
    updateWidgetCartService().then((results)=>{
        $(".cart-bag").html(results.count);
        $(".cart-amount").html(new Intl.NumberFormat().format(results.amount)+'đ');
    })
}
jQuery(function ($) {
    const activeRangePrice = (range)=>{
        $(".product-model").find(".wrap-price").find("ins").each((index,target)=>{
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
    const updateTotalLable = (total,amount)=>{
        $("#total").html(total);
        $("#amount").html(new Intl.NumberFormat().format(amount)+'đ')
    }
    const activeGroup = (variantElement,remove)=>{
        if (remove){
            $(".vh-nav-item").each((index,target)=>{
                const frame = $(target).find(".frame")
                if (frame.length>0){
                    frame.remove();
                }
            })
            return;
        }
        const group = variantElement.parent().attr("tabcontent")
        if (group) {
            let totalQuantity = 0;
            const variants = variantElement.parent().find(".variant");
            variants.each((index, target) => {
                const input = $(target).find(".js-result");
                if (input.val()) {
                    totalQuantity += parseInt(input.val())
                }
            })
            $(".vh-nav-item").each((index,target)=>{
                const tabId = $(target).attr("tabid");
                if (tabId===group){
                    if (totalQuantity>0){
                        if ($(target).find(".frame").length<=0){
                            $(target).append(`<div class="frame"></div>`)
                        }
                    }else {
                        const frame = $(target).find(".frame")
                        if (frame.length>0){
                            frame.remove();
                        }
                    }
                }
            })
        }

    }
    const quantityOnChange = (target)=>{
        const input = $(target);
        const variantElement = input.parents(".variant");
        const variantId = variantElement.data("id");
        const variantPrice = variantElement.data("price");
        const data = $(".product-model").data("model");
        if (data.isRetailt) {
            let total = 0;
            let amount = 0;
            $(".summary .js-quantity").each((e, quantityTarget) => {
                const quantityElement = $(quantityTarget);
                const quantityVariantElement = quantityElement.parents(".variant")
                const price = quantityVariantElement.data("price");
                const value = parseInt(quantityElement.find(".js-result").val());
                total += value
                amount += value * price;

            });
            updateTotalLable(total,amount)
        } else {
            let total = 0;
            let amount = 0;
            $(".summary .js-quantity").each((e, quantityTarget) => {
                const quantityElement = $(quantityTarget);
                const value = parseInt(quantityElement.find(".js-result").val());
                total += value
            });
            const dataRange = getRange(total+data.incart,data);
            activeRangePrice(dataRange.range);
            updateTotalLable(total,total*dataRange.price);
        }
            activeGroup(variantElement);
    }
    const notifyaddtocart = (success,message)=>{
        const  html = `<div class="toast-addtocart alert ${success?'alert-success':'alert-danger'}" role="alert">
                             ${message} 
                           </div>`
        const  element = $(html)
        element.insertBefore($(".btn-addtocart").parent().parent());
        setTimeout(()=>{element.remove()},2000)
    }
    const addToCart = (e)=>{
        const data = $(".product-model").data("model");
        let dataRequest = {
            productId:data.id,
            detailts:[],
            total:data.incart
        }
        let isAddToCart = false;
        $(".summary .js-quantity").each((e, quantityTarget) => {
            const quantityElement = $(quantityTarget);
            const variantElement = $(quantityElement.parents(".variant"))
            const id = variantElement.data("id")
            const value = parseInt(quantityElement.find(".js-result").val());
            if (value>0){
                dataRequest.detailts.push({
                    quantity:value,
                    variantId:id
                });
                dataRequest.total+=value;
            }
            quantityElement.find(".js-result").val(0)
        });

        if(dataRequest.total>=data.conditiondefault){
            isAddToCart=true;
        }
        if (isAddToCart){
            const cart = $('.cart-widget');
            const imgtodrag = $($(".slick-list").find("img")[0]);
            let imgclone = "";
            if (cart.length>0){
                if (imgtodrag) {
                    imgclone = imgtodrag.clone()
                    imgclone.offset({
                        bottom: imgtodrag.offset().bottom,
                        left: imgtodrag.offset().left
                    })
                    imgclone.css({
                        'opacity': '0.5',
                        'position': 'absolute',
                        'height': '150px',
                        'width': '150px',
                        'z-index': '100',
                        'left': '50%',
                        'top': '50%',
                    })
                    imgclone.prependTo($(document.body))
                    imgclone.animate({
                        'top': cart.offset().top,
                        'left': cart.offset().left,
                        'width': 75,
                        'height': 75
                    }, 500);

                    imgclone.animate({
                        'width': 0,
                        'height': 0
                    }, function () {
                        $(this).detach()
                    });

                }
            }

            addtocartService(dataRequest).then((results)=>{

                if (results.success){
                    notifyaddtocart(true,"Thêm giỏ hàng thành công!");
                    updateWidget();
                    activeGroup(null,true);
                }else {
                    if (results.code==='LOGIN'){
                        $(document.body).trigger("user.login");
                    }else {
                        notifyaddtocart(false,"Thêm giỏ hàng thất bại!")
                    }

                }
            })
        }else {
            notifyaddtocart(false,"Tối thiểu "+data.conditiondefault+" sản phẩm");
        }
    }
    const updateCart = (target)=>{
        const productElement = $(target).parents(".product-cart");
        const categoryElement = $(target).parents(".category-cart");
        const variantId = $(target).parents(".variant").data("variant");
        const productId = productElement.data("productid");
        const value = $(target).val();

        updateCartService({productId:productId, variantId:variantId, quantity:value}).then((results)=>{
            if (results.success){
                updateProductCart(productElement,results.cartProductList[0])
                getDelivery();
                if (results.activeProduct){
                    activeCheckbox(productElement,"product",results.activeProduct);
                }else {
                    activeCheckbox(productElement,"product",results.activeProduct);
                    activeCheckbox(categoryElement,"product",results.activeProduct);
                }


            }else {
                $(target).val(results.quantity);
            }

        })
    }
    const removeCart = (e)=>{
        const btnRemove = $(e.target);
        const productElement = btnRemove.parents(".product-cart")
        const categoryElement = productElement.parents(".category-cart");
        removeCartService([productElement.data("productid")]).then((results)=>{
            if (results.success){
                productElement.remove();
                if (categoryElement.find(".product-cart").length<=0)categoryElement.remove();
                updateWidget();
                getDelivery();
            }
        })
    }
    const activeCheckbox = (element,type,active)=>{
        element.find("input[name=checkboxcart]").each((index,target)=>{$(target).prop("checked",active);})
    }
    const activeCart = (e)=>{
        const checkbox = $(e.target);
        let categoryId,productId,variantId;
        const variantElement = checkbox.parents(".variant");
        const productElement = checkbox.parents(".product-cart");
        const categoryElement = checkbox.parents(".category-cart");
        const wrapCart = checkbox.parents(".cart-table");
        variantId = variantElement.data("variant");
        categoryId = categoryElement.data("categoryid");
        productId =  productElement.data("productid");
        let dataRequest = {active:checkbox.is(":checked")};

        if (categoryId) dataRequest.catId = categoryId;
        if (productId) dataRequest.productId = productId;
        if (variantId) dataRequest.variantId = variantId;

        activeCartService(dataRequest).then((results)=>{
            if (results.success){
                let check = true
                switch (checkbox.val()){
                    case "cat":
                        categoryElement.find("input[name=checkboxcart]").each((index,target)=>{
                            $(target).prop("checked",dataRequest.active);
                        })
                        break;
                    case "product":
                        productElement.find("input[name=checkboxcart]").each((index,target)=>{$(target).prop("checked",dataRequest.active);})
                        updateProductCart(productElement,results.cartProductList[0]);
                        categoryElement.find("input[name=checkboxcart][value=product]").each((index,target)=>{if (!$(target).is(":checked")) check= false;})
                        $(categoryElement.find("input[name=checkboxcart][value=cat]")[0]).prop("checked",check);
                        break;
                    case "variant":
                        updateProductCart(productElement,results.cartProductList[0]);
                        productElement.find("input[name=checkboxcart][value=variant]").each((index,target)=>{if (!$(target).is(":checked")) check= false;})
                        $(productElement.find("input[name=checkboxcart][value=product]")[0]).prop("checked",check);
                        categoryElement.find("input[name=checkboxcart][value=product]").each((index,target)=>{if (!$(target).is(":checked")) check= false;})
                        $(categoryElement.find("input[name=checkboxcart][value=cat]")[0]).prop("checked",check);
                        break
                }
            }else {
                activeCheckbox(categoryElement,"category",false);
                checkbox.prop("checked",false)
            }
            getDelivery()

        })
    }
    const updateProductCart = (productElement,product)=>{
        if (!product.retail){
            productElement.find(".range-item").each((index,target)=>{
                if ($(target).data("rangeindex")===product.range){
                    $(target).addClass("font-weight-bold")
                }else {
                    $(target).removeClass("font-weight-bold")
                }
            })
        }
        productElement.find(".product-amount").html(new Intl.NumberFormat().format(product.amountActive)+'đ');
        productElement.find(".total-quantity").html(product.quantity);
    }
    const isScrolledIntoView =(elem)=> {
        var docViewTop = $(window).scrollTop();
        var docViewBottom = docViewTop + $(window).height();
        var elemTop = $(elem).offset().top;
        var elemBottom = elemTop + $(elem).height();
        return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
    }
    const confirmPayment = (e)=>{
        const method = $("#method").val();
        const note = $("#note").val();
        confirmPaymentService({method:method,note:note}).then((results)=>{
            if (!results.success && results.fragment){
                const modalAddress = $("#addressModal")
                if (modalAddress.length>0) modalAddress.remove();
                const modal = $(results.fragment);
                $(document.body).append(modal);
                modal.modal("show")
                modal.find('select').selectpicker();
            } else if (!results.success){
                const alert = `<div class="alert alert-warning" role="alert">${'Vui lòng chọn sản phẩm trước khi thanh toán'}</div>`
                const elementAlert = $(alert);
                $("#confirmPayment").find(".modal-body").append(elementAlert);
                setTimeout(()=>{elementAlert.remove()},1000)
                console.log("khong the thanh toan vui long chonj san pham")
            }else {
                location.href = results.message;
            }
        })
    }
    const getDelivery = ()=>{
        getDeliveryService().then((results)=>{
            const shipSetting = results.shipsetting;
            let fee = results.fee;
            let discount = 0;
            if (shipSetting!=null && shipSetting.enable){
                if (shipSetting.shipType==='PERCENT'){
                    discount = parseInt(fee*new Number(shipSetting.shipValue)/100)
                }else {
                    discount = new Number(shipSetting.shipValue)
                }
            }
            fee = fee-discount;
            fee = fee>0?fee:0;
            let textShip = `<span>${new Intl.NumberFormat().format(fee)}đ</span>`
            if (discount && discount>0){
                textShip+=`(<del>${results.fee}đ</del>)`
            }
            $(".summary-category-active").html(results.categoryQuantityActive);
            $(".summary-quantity-active").html(results.totalQuantityActive);
            $(".summary-delivery").html(textShip);
            $(".summary-amount").html(new Intl.NumberFormat().format(fee+results.amountActive)+"đ");
            $(".summary-quantity").html(results.totalQuantity)
        })
    }

    const deleteAll = () => {
        let products    = $(".categories-cart input[type='checkbox'][value='product']:checked").parents(".product-cart");
        let categoris   = products.parents(".category-cart");
        let ids         = products.map((index,product) => $(product).data("productid"));

        removeCartService(Array.from(ids)).then((results)=>{
            if (results.success){
                products.remove();
                categoris.each((index, category) => {
                    if ($(category).find(".product-cart").length<=0) $(category).remove();
                });

                updateWidget();
                getDelivery();
            }
        })
    }

    $(window).scroll(function(){
        if (isScrolledIntoView($('.cart-footer'))){
            if ($('.footer-content').hasClass("active")){
                $('.footer-content').removeClass("active")
            }
        }else {
            if (!$('.footer-content').hasClass("active")){
                $('.footer-content').addClass("active")
            }
        }
    });
    $(document.body).on('quantity-cart', (e, target) => updateCart(target))
    $(document.body).on('quantity.onchange', (e, target) => quantityOnChange(target))
    $(document.body).on('click','.btn-addtocart', addToCart)
    $(document.body).on('change','input[name=checkboxcart]',activeCart)
    $(document.body).on('click','.ajax-remove',removeCart);
    $(document.body).on('click','.ajax-confirm-payment',confirmPayment)
    $(document.body).on('click', '.removeAll', deleteAll);
    $(document.body).on("address.onchange.address",()=>{
        getDelivery();
    })
    $(document.body).ready(function() {
        updateWidget();
        if (window.location.pathname==="/gio-hang"){
            getDelivery();
        }
    });
})
