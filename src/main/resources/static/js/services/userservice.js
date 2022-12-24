const loginService = async (username,password) =>{
    const params = new URLSearchParams({
        username:username,
        password:password
    })
    const responses = await fetch("/ajax/user/login?"+params.toString(),{method:"GET"});
    const data = await responses.json();
    return data;
}
const registerService = async (username,email,password,confirmPassword,phone)=>{
    const params = new URLSearchParams({
        userName:username,
        email:email,
        password:password,
        confirmPassword:confirmPassword,
        phone:phone
    })
    const responses = await fetch("/ajax/user/register?"+params.toString(),{method:"GET"});
    const data = await responses.json();
    return data;
}
const forgotPasswordService = async (username)=>{
    const responses = await fetch("/ajax/user/forgotpassword?username="+username,{method:"GET"});
    const data = await responses.json();
    return data;
}

