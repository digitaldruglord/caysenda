jQuery(function ($) {
    const isMobile = () => {
        let check = false;
        (function (a) {
            if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true;
        })(navigator.userAgent || navigator.vendor || window.opera);
        return check;
    };
    $(document.body).on('click', '.product-thumbnail-item', (e) => {
        const src = $(e.target).attr("src")
        const productElement = $(e.target).parents(".product-item__body");
        const imageProduct = productElement.find(".product-thumbnail");
        imageProduct.attr("src", src)
    })
    $(window).scroll((e) => {
        if (isMobile()) {
            const nav = $(".nav-logo-search-header");
            if ($(e.target).scrollTop() > 80) {
                if (!nav.hasClass("nav-position-fixed")) {
                    nav.addClass("nav-position-fixed")
                }
            } else {
                if (nav.hasClass("nav-position-fixed")) {
                    nav.removeClass("nav-position-fixed")
                }
            }
        }
    });
    $(document.body).on('submit', "#form-search-mobile", (e) => {
        e.preventDefault();
        const form = $('#form-search-mobile');
        const value = form.find('input').val();
        if (value){
            window.location.href = "/san-pham" + "?keyword=" + value
        }

    })
    $(document.body).on('click', "#submit-search-mobile", (e) => {
        e.preventDefault();
        const form = $('#form-search-mobile');
        const value = form.find('input').val();
        if (value){
            window.location.href = "/san-pham" + "?keyword=" + value
        }
    })
    $(document.body).on('submit', "#searchproduct-form", (e) => {
        e.preventDefault();
        const value = $("#searchproduct-item").val();
        const category = $("#searchcategory").val();
        if (category === 'all') {
            window.location.href = "/san-pham" + "?keyword=" + value
        } else {
            window.location.href = category + "?keyword=" + value
        }
    })

    window.onclick = (e)=>{
        if ($(".wrap-content-search").find($(e.target)).length<=0){
            $(".wrap-content-search").addClass("d-none");
        }
        if ($(e.target).is($("#searchproduct-item"))){
            $(".wrap-content-search").removeClass("d-none");
        }
    }
    const renderContentSearchMD = (data)=>{
        const elements = data.map((item)=>{
            return(`<a class="d-block" href="/san-pham?keyword=${item}"><span class="ml-3">${item}</span></a>`)
        }).join("")
        $(".content-search-form").empty();
        $(".content-search-form").append(elements);
    }
    const renderContentSearchSM = (data)=>{
        const elements = data.map((item)=>{
            return(`<a class="d-block" href="/san-pham?keyword=${item}"><i class="fas fa-search ml-3"></i><span class="ml-3">${item}</span></a>`)
        }).join("")
        $(".content-search-sm").empty();
        $(".content-search-sm").append(elements);
    }
    let typingTimer;                //timer identifier
    let doneTypingInterval = 200;  //time in ms (5 seconds)
    $(document.body).on('keyup','#searchproduct-item',(e)=>{
        const value = e.target.value;
        clearTimeout(typingTimer);
        typingTimer = setTimeout(()=>{
            findAllByKeywordService(value).then((results)=>{
                renderContentSearchMD(results.data);
            })
        }, doneTypingInterval);

    })
    $(document.body).on('keyup','#input-value-search-sm',(e)=>{
        const value = e.target.value;
        clearTimeout(typingTimer);
        typingTimer = setTimeout(()=>{
            findAllByKeywordService(value).then((results)=>{
                renderContentSearchSM(results.data);
            })
        }, doneTypingInterval);

    })
    $(document.body).on('click', "#searchProduct1", (e) => {
        e.preventDefault();
        const value = $("#searchproduct-item").val();
        const category = $("#searchcategory").val();
        if (category === 'all') {
            window.location.href = "/san-pham" + "?keyword=" + value
        } else {
            window.location.href = category + "?keyword=" + value
        }
    })
    $(".vh-nav-item").click((e) => {
        let element = $(e.target);
        if (element.hasClass("frame")) {
            element = element.parent();
        }
        const tabId = element.attr("tabid");
        $(".vh-nav-item").each((index,target)=>{
            const targetId = $(target).attr("tabid");
            if (tabId===targetId){
                $(target).addClass("active")
            }else {
                $(target).removeClass("active")
            }
        })
        $(".vh-nav-tab").each((index, target) => {
            const tabContentElement = $(target);
            const tabContentId = tabContentElement.attr("tabcontent");
            if (tabContentId === tabId) {
                tabContentElement.addClass("show")
            } else {
                tabContentElement.removeClass("show")
            }
        })
    })
    $(document.body).on('change', '.selectpickerfilter', (e) => {
        const element = $(e.target);
        const params = new URLSearchParams(window.location.search);
        if (!params.has("sort")) {
            params.append("sort", element.val())
        } else {
            params.set("sort", element.val())
        }
        window.location.href = window.location.origin + window.location.pathname + "?" + params.toString();
    });
    $(document.body).on('change', '.selectpickerpagesize', (e) => {
        const element = $(e.target);
        const params = new URLSearchParams(window.location.search);
        if (!params.has("pageSize")) {
            params.append("pageSize", element.val())
        } else {
            params.set("pageSize", element.val())
        }
        window.location.href = window.location.origin + window.location.pathname + "?" + params.toString();
    })
    $(document.body).on('submit', '.form-next-page', (e) => {
        e.preventDefault();
        const input = $(e.target).find(".input-next-page").val();
        const params = new URLSearchParams(window.location.search);
        if (!params.has("page")) {
            params.append("page", input)
        } else {
            params.set("page", input)
        }
        window.location.href = window.location.origin + window.location.pathname + "?" + params.toString();
    })
    $(document.body).on('click', '.btn-next-page', (e) => {
        e.preventDefault();
        const wrap = $(e.target).parent();
        const input = wrap.find(".input-next-page").val();
        console.log(input);
        const params = new URLSearchParams(window.location.search);
        if (!params.has("page")) {
            params.append("page", input)
        } else {
            params.set("page", input)
        }
        window.location.href = window.location.origin + window.location.pathname + "?" + params.toString();
    })
    $(document.body).on('addloadding', () => {
        $(document.body).append(`<div class="loadding">
                                    <svg width="200" version="1.1" id="L1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px" viewBox="0 0 100 100" enable-background="new 0 0 100 100" xml:space="preserve">
                                        <circle fill="none" stroke="#fff" stroke-width="6" stroke-miterlimit="15" stroke-dasharray="14.2472,14.2472" cx="50" cy="50" r="47" >
                                          <animateTransform attributeName="transform" attributeType="XML" type="rotate" dur="5s" from="0 50 50" to="360 50 50" repeatCount="indefinite" />
                                      </circle><circle fill="none" stroke="#fff" stroke-width="1" stroke-miterlimit="10" stroke-dasharray="10,10" cx="50" cy="50" r="39">
                                          <animateTransform attributeName="transform" attributeType="XML" type="rotate" dur="5s" from="0 50 50" to="-360 50 50" repeatCount="indefinite" />
                                      </circle><g fill="#fff">
                                      <rect x="30" y="35" width="5" height="30">
                                          <animateTransform attributeName="transform" dur="1s" type="translate" values="0 5 ; 0 -5; 0 5" repeatCount="indefinite" begin="0.1"/>
                                      </rect><rect x="40" y="35" width="5" height="30" >
                                        <animateTransform attributeName="transform" dur="1s" type="translate" values="0 5 ; 0 -5; 0 5" repeatCount="indefinite" begin="0.2"/>
                                      </rect><rect x="50" y="35" width="5" height="30" >
                                        <animateTransform attributeName="transform" dur="1s" type="translate" values="0 5 ; 0 -5; 0 5" repeatCount="indefinite" begin="0.3"/>
                                      </rect><rect x="60" y="35" width="5" height="30" >
                                        <animateTransform attributeName="transform" dur="1s" type="translate" values="0 5 ; 0 -5; 0 5" repeatCount="indefinite" begin="0.4"/>
                                      </rect><rect x="70" y="35" width="5" height="30" >
                                        <animateTransform attributeName="transform" dur="1s" type="translate" values="0 5 ; 0 -5; 0 5" repeatCount="indefinite" begin="0.5"/>
                                      </rect>
                                      </g>
                                    </svg>
                                </div>`)
    })
    $(document.body).on('removeloadding', () => {
        $(".loadding").remove();
    })
    $("#product-quickview").modal("show");
    $(document.body).on('click','#loginbtn',(e)=>{
        e.preventDefault();
        console.log("login")
    });
    $(document.body).on('click','#registerbtn',(e)=>{
        e.preventDefault();
        console.log("register")
    });
    $(document.body).on('click','#testId',(e)=>{
        e.preventDefault();
        console.log("register")
    });
    $(document.body).on('user.login',()=>{
        console.log("running")
        if (isMobile()) {
            window.location.href="/dang-nhap"
        }else {
            $("#sidebarNavToggler").trigger("click.HSUnfold")
            const quickview = $("#product-quickview");
            if (quickview.length>0){
                quickview.modal("hide");
            }
        }
    })
})