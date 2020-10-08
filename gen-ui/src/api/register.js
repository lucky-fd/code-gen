// 注册规则
const rules = {
    username: [
        {required: true, trigger: "blur", message: "请输入您的账号/电话号码"},
        {
            validator: function(rule, value, callback) {
                if (/^(13[0-9]|14[0-9]|15[0-9]|16[6]|18[0-9]|19[6,9]|17[0-9])\d{8}$/i.test(value) == false) {
                    callback(new Error("请输入正确的手机号"));
                } else {
                    //校验通过
                    callback();
                }
            },
            trigger: "blur"
        }
    ],
    nickName: [
        {required: true, trigger: "blur", message: "请输入您的昵称"},
        {min: 2, max: 20, message: '用户昵称长度必须介于 2 和 20 之间', trigger: 'blur'}
    ],

    sex: [
        {required: true, trigger: "blur", message: "请输入您的性别"},
    ],
    age: [
        {required: true, trigger: "blur", message: "请输入您的年龄"},
    ],
    birthday: [
        {required: true, trigger: "blur", message: "请输入您的生日"},
    ],
    phonenumber: [
        {required: true, trigger: "blur", message: "请输入您的私人电话"},
        {
            validator: function(rule, value, callback) {
                if (/^(13[0-9]|14[0-9]|15[0-9]|16[6]|18[0-9]|19[6,9]|17[0-9])\d{8}$/i.test(value) == false) {
                    callback(new Error("请输入正确的手机号"));
                } else {
                    //校验通过
                    callback();
                }
            },
            trigger: "blur"
        }
    ],
    email: [
        {required: true, trigger: "blur", message: "请输入您的qq邮箱"},
        {
            validator: function(rule, value, callback) {
                if (/^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(value) == false) {
                    callback(new Error("请输入正确的邮箱"));
                } else {
                    //校验通过
                    callback();
                }
            },
            trigger: "blur"
        }
    ],
    avatar: [
        {required: true, trigger: "blur", message: "请上传您的照片"},
    ],
    education: [
        {required: true, trigger: "blur", message: "请输入您的最高学历"},
    ],
    educationSchool: [
        {required: true, trigger: "blur", message: "请输入您的最高学历学校"},
    ],
    major: [
        {required: true, trigger: "blur", message: "请输入您的最高学位专业"},
    ],
    census: [
        {required: true, trigger: "blur", message: "请输入您的户籍"},
    ],
    censusType: [
        {required: true, trigger: "blur", message: "请输入您的户口类型"},
    ],
    natives: [
        {required: true, trigger: "blur", message: "请输入您的籍贯"},
    ],
    homeAddress: [
        {required: true, trigger: "blur", message: "请输入您的家庭住址"},
    ],
    liveAddress: [
        {required: true, trigger: "blur", message: "请输入您的个人详细地址"},
    ],
    marry: [
        {required: true, trigger: "blur", message: "请输入您的婚姻状态"},
    ],
    political: [
        {required: true, trigger: "blur", message: "请输入您的政治面貌"},
    ],
    hurry: [
        {required: true, trigger: "blur", message: "请输入您的紧急联系人"},
    ],
    hurryTel: [
        {required: true, trigger: "blur", message: "请输入您的紧急联系人电话"},
        {
            validator: function(rule, value, callback) {
                if (/^(13[0-9]|14[0-9]|15[0-9]|16[6]|18[0-9]|19[6,9]|17[0-9])\d{8}$/i.test(value) == false) {
                    callback(new Error("请输入正确的手机号"));
                } else {
                    //校验通过
                    callback();
                }
            },
            trigger: "blur"
        }
    ],
    hurryType: [
        {required: true, trigger: "blur", message: "请输入您与紧急联系人的关系"},
    ],
    identity: [
        {required: true, trigger: "blur", message: "请输入您的身份证号"},
        {
            validator: function(rule, value, callback) {
                if (/^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/i.test(value) == false) {
                    callback(new Error("请输入正确的身份证号"));
                } else {
                    //校验通过
                    callback();
                }
            },
            trigger: "blur"
        }
    ],
    bandCard: [
        {required: true, trigger: "blur", message: "请输入您的银行卡号"},
    ],
    nationality: [
        {required: true, trigger: "blur", message: "请输入您的民族"},
    ],
    password: [
        {required: true, trigger: "blur", message: "请输入您的密码"},
        {min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur'}
    ],
    confirmPassword: [
        {required: true, trigger: "blur", message: "请再次输入您的密码"},
    ],
    code: [{required: true, trigger: "change", message: "请输入验证码"}]
}

export {
    rules
}
