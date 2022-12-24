jQuery(function ($) {
    $("#sliderSyncingNav").on('init', (event,sd)=>{
        console.log(event);
    })
    $(document.body).on("click",".slick-goto",()=>{
        $("#sliderSyncingNav").slick('slickGoTo', 3);

    })
})