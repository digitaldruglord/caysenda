
jQuery(function ($) {
    const login = (e)=>{
        e.preventDefault();
        const email = $("#signinEmail").val();
        const password = $("#signinPassword").val();
        $(document.body).trigger('addloadding')

        loginService(email,password).then((results)=>{
            console.log(results)
           if (results.success){
               window.location.reload();
           }
            $(document.body).trigger('removeloadding')
        })
    }
    const register = (e)=>{
        e.preventDefault();
        const username = $("#signupUsername").val();
        const email = $("#signupEmail").val();
        const password = $("#signupPassword").val();
        const confirmPassword = $("#signupConfirmPassword").val();
        const phone = $("#signupPhone").val();
        $(document.body).trigger('addloadding')
        registerService(username,email,password,confirmPassword,phone).then((results)=>{
               const notify = $("#signup").find(".notification");
               const color = results.success?'alert-success':'alert-danger'
               const element = `<div class="alert ${color}" role="alert">
                                   <span>${results.message}</span>
                                 </div>`
               notify.empty();
               notify.append(element);
               if (results.success){
                   setTimeout(()=>{window.location.reload()},1000)
               }
            $(document.body).trigger('removeloadding')
        })
    }
    const forgotPassword = (e)=>{
        e.preventDefault();
        const input = $("#recoverEmail").val();
        forgotPasswordService(input).then((results)=>{
           $("#forgotPassword").prepend(
              ` <div class="alert alert-success">Đã gửi thông tin khôi phục mật khẩu về email của bạn</div>`
           )
        })
    }

    $(document.body).on('click','.ajax-login',login);
    $(document.body).on('click','.ajax-register',register);
    $(document.body).on('click','.ajax-forgotpassword',forgotPassword)
})