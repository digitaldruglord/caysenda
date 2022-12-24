window.onload = ()=> {
    const notification = (status,message)=>{
        const notify = document.querySelector(".buynow-notification");
        notify.classList.add("d-flex","justify-content-center")
        if (notify){
            notify.innerHTML = ``
            const span  = document.createElement("span");
            span.classList.add("alert","font-weight-bold","rounded","text-black",status?"alert-success":"alert-danger");
            span.appendChild(document.createTextNode(message));
            notify.prepend(span)
        }

    }
    const checkField = (data)=>{

        const filter = Object.keys(data).filter((item)=>{
            return data[item]===''
        })

        return filter.length>0?false:true
    }
    const requestBuynow = async (data)=>{
        const header = {'Content-Type': 'application/json'}
        const response = await fetch("/ajax/order/buy-now",{method:"POST",headers:header,body:JSON.stringify(data)});
        console.log(response);
        return await response.json();
    }
    if (document.querySelector(".buynow-button")){
        document.querySelector(".buynow-button").onclick = (e)=>{
            const button = e.target;
            const form = document.querySelector(".buynow-form");
            const input = [...form.querySelectorAll("input[field],select")];

            const map = input.map((item)=>{
                const key = item.getAttribute("field");
                const value = item.value;
                return{
                    key:key,
                    value:value
                }
            }).reduce((dataconvert,item)=>{
                dataconvert.set(item.key,item.value);
                return dataconvert;
            },new Map());

            const data = Object.fromEntries(map);
            data.productId = form.dataset.productid;
            if (!checkField(data)){
                notification(false,"Vui lòng điền đầy đủ thông tin để tiến hành mua hàng")
            }else {
                requestBuynow(data).then((results)=>{
                    console.log(results);
                    if (results.success){
                        notification(true,"Bạn đã đặt hàng thành công");
                        setTimeout(()=>{
                            window.location.href = "/don-hang/"+results.id
                        },300)

                    }else {
                        notification(false,"Tạo đơn hàng không thành công vui lòng liên hệ với cửa hàng");
                    }

                })
            }



        }
    }

    if (document.querySelector("a")){
        document.querySelector("a").onclick = (e)=>{
            const href = e.target.getAttribute("href");
            if (href.includes("#")){
                e.preventDefault();
                if (document.body.querySelector(href)){
                    document.body.querySelector(href).scrollIntoView({
                        behavior: 'smooth'
                    });
                }

            }


        }
    }


};
