const getQuickviewService = async (productId)=>{
    const responese = await fetch("/ajax/cart/quickview?id="+productId,{method:"GET"});
    return await responese.json();
}
