jQuery(function ($) {
    const lazy = ()=>{
        setTimeout(()=>{
            $('.product-image-lazy').lazy({
                effect: "fadeIn",
                effectTime: 500,
                threshold: 0,
            });
        },100)

    }
    const fetchTab = async (tab,catid)=>{
        let object = {tab:tab}
        if (catid){object.categoryId = catid}
        const params = new URLSearchParams(object)
        const responses = await fetch("/ajax/lazy/product-lazy?"+params.toString(),{method:"GET"});
        const data = await responses.json();
        return data;
    }
    $('a[data-toggle="pill"][nav-classic="true"]').on('shown.bs.tab', function (e) {
        const element = $(e.target);
        const tabContent = $(document.body).find(element.attr("href"));
        const loadding = tabContent.find(".loadding-tab").length>0;
        switch (element.attr("tab")){
            case "new":
                if (loadding){
                    fetchTab(element.attr("tab")).then((results)=>{
                        tabContent.empty();
                        tabContent.append(results.fragment);
                        lazy();
                    })
                }
                break;
            case "bestselling":
                if (loadding){
                    fetchTab(element.attr("tab")).then((results)=>{
                        tabContent.empty();
                        tabContent.append(results.fragment);
                        lazy();
                    })
                }
                break;
            case "appreciate":
                if (loadding){
                    fetchTab(element.attr("tab")).then((results)=>{
                        tabContent.empty();
                        tabContent.append(results.fragment);
                        lazy();
                    })
                }
                break;

        }

    })
    if ($('.products-group.lazy').length>0){
        $('.products-group.lazy').lazy({
            effect: "slideDown",
            effectTime: 500,
            threshold: 0,

            onError: function(element) {
            },
            ajaxLoadCategory: function(element, response) {
                const tab = element.data("tab");
                fetchTab(tab).then(results=>{
                    element.empty()
                    element.replaceWith(results.fragment);
                    response(true);

                    $('a[data-toggle="pill"][data-tab="category"]').on('shown.bs.tab',(e)=> {
                        const element = $(e.target)
                        const id = $(e.target).data("id");
                        const tabContent = $(document.body).find(element.attr("href"));
                        const loadding = tabContent.find(".loadding-content").length>0;
                        if (loadding){
                            fetchTab("framecategorytab",id).then((results)=>{
                                tabContent.empty();
                                tabContent.append(results.fragment)
                            }).finally(()=>{
                                lazy()
                            })
                        }
                    })
                    if (tab==='framecategory'){
                        setTimeout(()=>{
                            setInterval(()=>{
                                const elements = $(".random-tab");
                                const rand = Math.floor(Math.random() * elements.length);
                                $(elements[rand]).trigger("click");
                            },30000)
                        },30000)
                    }
                })
            },
            onFinishedAll: function (e){
                lazy();
                if( !this.config("autoDestroy") )
                    this.destroy();
            }
        });
    }

    $('.slick.lazy').lazy({
        effect: "slideDown",
        effectTime: 500,
        threshold: 0,

        onError: function(element) {
        },
        ajaxloader: function(element, response) {
            const tab = element.data("tab");
            const categoryId = element.data("category")
            fetchTab(tab,categoryId).then(results=>{
                element.empty()
                element.replaceWith(results.fragment);
                response(true);
                if ($(".related-slick").length>0){
                    if (!$(".related-slick").data("init")){
                        $.HSCore.components.HSSlickCarousel.init('.related-slick');
                        $(".related-slick").data("init",true)
                    }
                }
                if ($(".bestseller-slick").length>0){
                    if (!$(".bestseller-slick").data("init")){
                        $.HSCore.components.HSSlickCarousel.init('.bestseller-slick');
                        $(".bestseller-slick").data("init",true)
                    }
                }
                lazy();

            })

        },
        onFinishedAll: function (e){
            if( !this.config("autoDestroy") )
                this.destroy();
        }
    });
    lazy();
    $(document.body).on("onLazyLoadProductImage",()=>{
        lazy();
    })
})
