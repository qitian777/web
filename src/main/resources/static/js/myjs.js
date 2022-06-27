// JavaScript Document
function changeSrc() {
    $(".card-img-top").each(
        function () {
            let s = $(this).attr("src");
            s = check_replace(s);
            $(this).attr("src", s);
        }
    )
}

function setHeight() {
    let ds = $(".img_scale")
    let w = ds.eq(0).width() * 1.4;
    ds.height(w);
    $(".card-img-top").height(w);
}

function href_selected(href, para, para_value, para_pl) {
    let selector = "." + para + "s>li>a"
    $(selector).each(      //地区链接添加href
        function () {
            let para_text = $(this).text();
            if (para_text === "全部") {
                $(this).attr("href", href + para_pl);
                if (para_value === "") {
                    $(this).attr("class", "selected");
                }
            } else {
                $(this).attr("href", href + "&" + para + "=" + para_text + para_pl);
                if (para_value === para_text) {
                    $(this).attr("class", "selected");
                }
            }
        }
    )
}

function set_page_href(href) {
    let current_page = $("#hi-go_page").val();
    let total_page = $("#hi-page").val();
    let page = 0;
    $(".page-item>a").each(
        function () {
            let page_text = $.trim($(this).text());
            if (page_text === "上一页") {
                if (current_page < 2) {
                    $(this).parent().attr("class", "page-item disabled")
                } else {
                    page = parseInt(current_page) - 1
                    $(this).attr("href", href + "&page=" + page)
                }
            } else if (page_text === "下一页") {
                if (current_page === total_page) {
                    $(this).parent().attr("class", "page-item disabled")
                } else {
                    page = parseInt(current_page) + 1
                    $(this).attr("href", href + "&page=" + page)
                }
            } else {
                $(this).attr("href", href + "&page=" + page_text)
                if (page_text === current_page) {
                    $(this).parent().attr("class", "page-item active")
                }
            }
        }
    )
}

function change_href() {
    let logged = $("#hi-user").val()
    let pa = {};
    let type = $("#hi-type").val()
    let order00 = $("#hi-order").val();
    if (type === "") {
        return;
    }
    let href = "";
    if (logged===""){
        href = "/list?" + "type=" + type;
    }else{
        let type01;
        $(".coll-type>li>a").each(
           function () {
               type01 = $(this).text();
               href= "/user/coll?" + "type=" + type01;
               $(this).attr("href", href + "&order=" + order00);
               if (type===type01){
                   $(this).attr("class", "selected");
               }
           }
        )
        href= "/user/coll?" + "type=" + type;
    }
    let page_param = ""
    pa['area'] = $("#hi-area").val();
    pa['style'] = $("#hi-style").val();
    pa['year'] = $("#hi-year").val();
    pa['order'] = order00;
    let pl = {'area': '', 'style': '', 'year': '', 'order': ''}
    for (let key1 in pl) {
        for (let key2 in pa) {
            if (key1 !== key2 && pa[key2] !== "") {
                pl[key1] = pl[key1] + "&" + key2 + "=" + pa[key2]
            }
        }
    }
    for (let key in pa) {
        if (pa[key] !== "") {
            page_param = page_param + "&" + key + "=" + pa[key]
        }
    }
    href_selected(href, "area", pa["area"], pl["area"]);
    href_selected(href, "style", pa["style"], pl["style"]);
    href_selected(href, "year", pa["year"], pl["year"]);
    href_selected(href, "order", pa["order"], pl["order"]);
    set_page_href(href + page_param);
}


function set_index_href() {
    let href = "/list?type=";
    let types = {"movie": "电影", "drama": "电视剧", "animation": "动漫"};
    for (let key in types) {
        $("." + key + "-ul>li>a").each(
            function () {
                let style = $(this).text();
                $(this).attr("href", href + types[key] + "&style=" + style);
            }
        )
    }
}

function check_replace(src) {
    if (src.indexOf("http://") !== -1) {
        src = src.replace("http://", "https://images.weserv.nl/?url=");
    } else if (src.indexOf("https://") !== -1) {
        src = src.replace("https://", "https://images.weserv.nl/?url=");
    }
    return src;
}

function change_detail_img() {
    let s = $("img[class='w-100']");
    let src = check_replace(s.attr("src"));
    s.attr("src", src);
}

function replace_space() {
    let he = 0;
    let ss = $("div[class='w-50 float-left']>div>span")
    ss.each(
        function () {
            let text = $(this).text();
            text = text.replaceAll("\n", "<br>");
            $(this).text("");
            $(this).append(text);
            if (he < $(this).parent().height()) {
                he = $(this).parent().height()
            }
        }
    )
    ss.each(
        function () {
            $(this).parent().height(he)
        }
    )
}

function set_search_page_href() {
    let search = $("#hi-search").val();
    if (search !== "") {
        set_page_href("/search?string=" + search);
    }
}

function getEmailCode() {
    getMailCode("/getCode", false);
}

function getMailCode(url, reset) {
    let email = $("input[type='email']").val()
    let label = $("#mail-error");
    if (email !== "") {
        $.ajax({
            url: url,
            type: "post",
            dataType: "text",
            data: {"email": email},
            success: function (data) {
                let button = $("#code-button");
                if (data === "0") {
                    if (reset) {
                        label.text("该邮箱未被注册！");
                    } else {
                        label.text("该邮箱已被注册！");
                    }
                    label.attr("class", "text-danger");
                } else {
                    label.attr("class", "invisible");
                    let count = 30;
                    let time = setInterval(function () {
                        count = count - 1;
                        if (count === 0) {
                            button.text("重新发送");
                            button.attr("disabled", false)
                            clearInterval(time);
                        } else {
                            button.text("已发送(" + count + ")");
                            button.attr("disabled", true)
                        }
                    }, 1000)
                }
            }
        })
    } else {
        label.text("邮箱不能为空！");
        label.attr("class", "text-danger");
    }
}

function getResetEmailCode() {
    getMailCode("/getResetCode", true);
}

function checkName(boo) {
    let name = $("input[name='nickname']").val().trim();
    let nameLabel = $("#name-error");
    if (name.length < 3 || name.length > 9) {
        nameLabel.attr("class", "text-danger");
        boo = false;
    } else {
        nameLabel.attr("class", "invisible");
    }
    return boo;
}

function checkEmail(boo) {
    let username = $("input[name='username']").val().trim();
    let mailLabel = $("#mail-error");
    if (username === "") {
        mailLabel.attr("class", "text-danger");
        mailLabel.text("邮箱不能为空！");
        boo = false;
    } else {
        mailLabel.attr("class", "invisible");
    }
    return boo;
}

function checkCode(boo) {
    let code = $("input[name='code']").val().trim();
    let codeLabel = $("#code-error");
    let codeReg = /^[0-9]{6}$/;
    if (!codeReg.test(code)) {
        codeLabel.attr("class", "text-danger");
        boo = false;
    } else {
        codeLabel.attr("class", "invisible");
    }
    return boo;
}

function checkPw(boo) {
    let password1 = $("input[name='password']").val();
    let password2 = $("input[name='confirm-password']").val();
    let passwordLabel = $("#password-error");
    if (password1 !== password2) {
        passwordLabel.attr("class", "text-danger");
        passwordLabel.text("两次输入密码不一致！");
        boo = false;
    } else {
        passwordLabel.attr("class", "invisible");
    }
    let passwordReg = /^[A-z][\w]{6,14}$/
    if (!passwordReg.test(password1)) {
        passwordLabel.attr("class", "text-danger");
        passwordLabel.text("请按提示设置密码！");
        boo = false;
    }
    return boo;
}

function checkLoginForm() {
    let boo = true;
    // 检查用户名
    boo = checkName(boo);
    // 检查邮箱
    boo = checkEmail(boo);
    // 检查验证码
    boo = checkCode(boo);
    // 检查密码
    boo = checkPw(boo);
    return boo;
}

function checkUpdateInfo() {
    let boo = true;
    let i = 1;
    let nickname = $("input[name='nickname']");
    if (nickname.val() !== "" && nickname.val() !== nickname.attr("placeholder")) {
        boo = checkName(boo);
        i++;
    }
    let pw = $("input[name='password']")
    if (pw.val() !== "") {
        boo = checkPw(boo);
        i++;
    }
    if (i > 1) {
        return boo;
    } else {
        return false;
    }
}

function resetPw() {
    let boo = true;
    boo = checkCode(boo);
    boo = checkPw(boo);
    return boo;
}

function checkStore() {
    let nickname = $(".dropdown-item span").text();
    if (nickname !== "") {
        let id = parseInt($("#hi-id").val());
        if (id > 0) {
            $.ajax({
                url: "/user/checkStore",
                type: "post",
                dataType: "text",
                data: {"id": id},
                success: function (data) {
                    if (data === "1") {
                        $(".store use").attr("xlink:href", "#icon-jushoucang")
                    }
                }
            })
        }
    }
}

function storeUp() {
    let nickname = $(".dropdown-item span").text();
    if (nickname !== "") {
        let id = parseInt($("#hi-id").val());
        let store = true;
        if ($(".store use").attr("xlink:href") !== "#icon-shoucang2") {
            store = false;
        }
        $.ajax({
            url: "/user/storeUp",
            type: "post",
            dataType: "text",
            data: {"id": id, "store": store},
            success: function (data) {
                if (data === "a1") {
                    $(".store use").attr("xlink:href", "#icon-jushoucang")
                } else if (data === "d1") {
                    $(".store use").attr("xlink:href", "#icon-shoucang2")
                }
            }
        })
    } else {
        window.location.href = "/logon"
    }
}

function detailImgHeight(){
    let imgHeight=$(".d-img>img").height();
    $(".d-img").height(imgHeight);
}

$(document).ready(
    function () {
        try {
            change_detail_img()
            detailImgHeight()
            replace_space()
        } catch (e) {
        }
        try {
            set_search_page_href();
        } catch (e) {
        }
        try {
            checkStore();
        } catch (e) {
        }
    }
)
window.onload = function () {
    try{
        setHeight();
        changeSrc();
    }catch (e) {
    }
    try {
        change_href();
    } catch (e) {
    }
    try {
        set_index_href();
    } catch (e1) {
    }
}
window.onresize = function () {
    try{
        setHeight();
    }catch (e) {
    }
    try{
        detailImgHeight();
    }catch (e) {}
}