const getAddressDataService = async (params) =>{
    const paramsRequest = new URLSearchParams(params)
    const responses = await fetch("/ajax/address?"+paramsRequest.toString(),{method:"GET"});
    const data = await responses.json();
    return data;
}
const saveAddressService = async (params)=>{
    const responses = await fetch("/ajax/address",{method:"POST",headers:{'Content-Type': 'application/json'},body:JSON.stringify(params)});
    const data = await responses.json();
    return data;
}
const addressModalService = async (params)=>{
    const paramsRequest = new URLSearchParams(params);
    const responses = await fetch("/ajax/address/load-modal?"+paramsRequest.toString(),{method:"GET"})
    const data = await responses.json();
    return data
}
const setDefaultAddressService = async (id)=>{
    const responses = await fetch("/ajax/address/default-address?id="+id,{method:"GET"})
    const data = await responses.json();
    return data
}
const removeAddressService = async (id)=>{
    const responses = await fetch("/ajax/address?addressId="+id,{method:"DELETE"})
    const data = await responses.json();
    return data
}