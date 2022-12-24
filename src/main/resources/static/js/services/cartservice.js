const addtocartService = async (params)=>{
    const responses = await fetch("/ajax/cart/addtocart",{method:"POST",headers:{'Content-Type': 'application/json'},body:JSON.stringify(params)})
    const data = await responses.json();
    return data
}
const updateCartService = async (params)=>{
    const paramsRequest = new URLSearchParams(params);
    const responses = await fetch("/ajax/cart/update?"+paramsRequest.toString(),{method:"GET"})
    const data = await responses.json();
    return data

}
const activeCartService = async (params)=>{
    const paramsRequest = new URLSearchParams(params);
    const responses = await fetch("/ajax/cart/active?"+paramsRequest.toString(),{method:"GET"})
    const data = await responses.json();
    return data
}
const removeCartService = async (ids)=>{
    let params = ids.map((id) => "productId=" + id).join("&");
    const responses = await fetch("/ajax/cart/delete?" + params,{method:"GET"})
    const data = await responses.json();
    return data
}
const confirmPaymentService = async (params)=>{
    const paramsRequest = new URLSearchParams(params);
    const responses = await fetch("/ajax/cart/confirm?"+paramsRequest.toString(),{method:"GET"})
    const data = await responses.json();
    return data
}
const updateWidgetCartService = async ()=>{
    const responses = await fetch("/ajax/cart/update-widget",{method:"GET"})
    const data = await responses.json();
    return data
}
const getDeliveryService = async ()=>{
    const responses = await fetch("/ajax/cart/get-fee-delivery",{method:"GET"})
    const data = await responses.json();
    return data
}
