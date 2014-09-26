<%@ page import="com.eryansky.utils.AppConstants" %>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
    var queueIdago;
    $(function() {
        loadSex();
        up();
        var uploadpath=$("#uploadpath").val();
        if($('#photo').val()){
            $('#photo_pre').attr("src","${_ctx}/"+uploadpath+"/"+$('#photo').val());
        }

        $('#photo_pre').show();
        $(".uploadify").css({'display': 'inline-block', 'height': '24px', 'padding-right': '18px', 'outline': 'none'});
        if("${model.id}"==""){
            setSortValue();
        }
    });
    //性别
    function loadSex(){
        $('#sex').combobox({
            url: '${ctx}/sys/user/sexTypeCombobox?selectType=select',
            height:28,
            width: 120,
            editable:false,
            value:'2'
        });
    }

    //设置排序默认值
    function setSortValue() {
        $.get('${ctx}/sys/user/maxSort', function(data) {
            if (data.code == 1) {
                $('#orderNo').numberspinner('setValue',data.obj+1);
            }
        }, 'json');
    }

    var uploadLimit = 1;
    function up() {
        $('#file').uploadify({
            method: 'post',
            swf: '${ctxStatic}/js/uploadify/scripts/uploadify.swf',  //FLash文件路径
            buttonText: '浏  览',                                 //按钮文本
            uploader: '${ctx}/sys/user/upload;jsessionid=<%=session.getId() %>',
            fileObjName: 'filedata',
            removeCompleted: false,                             //选择文件后是否自动上传，默认为true
            multi: false,                                 //是否为多选，默认为true
            fileSizeLimit: '2MB',                       //单个文件大小，0为无限制，可接受KB,MB,GB等单位的字符串值
            fileTypeDesc: 'All Files',                 //文件描述
            fileTypeExts: '*.gif; *.jpg; *.png; *.bmp',  //上传的文件后缀过滤器
            onUploadStart: function (file) {
                if (queueIdago) {
                    $("#file").uploadify('cancel', queueIdago);
                }
            },

            //上传到服务器，服务器返回相应信息到data里
            onUploadSuccess: function (file, data, response) {

                jsonData = eval("(" + data + ")");
                filePath1 = jsonData.msg.url;
                queueIdago = file.id;
                var uploadpath = $("#uploadpath").val();
                $('#photo_pre').attr("src", "${_ctx}/" + uploadpath + "/" + jsonData.msg.url);
                $('#photo_pre').show();
//                $('#' + file.id).find('.data').html('上传完毕');
                $("#photo").val(filePath1);
                var cancel = $('#file-queue .uploadify-queue-item[id="' + file.id + '"]').find(".cancel a");
                if (cancel) {
                    cancel.click(function () {
                        $.post('${ctx}/sys/user/delFileUpload', {filePath: filePath1}, function (data) {
                            if (data.code == 1) {
                            } else {

                            }
                        }, 'json');

                    });
                }
            }

        });
    }
</script>
<div>
    <form id="user_form" class="dialog-form" method="post" novalidate>
        <input type="hidden" id="id" name="id" />
        <!-- 用户版本控制字段 version -->
        <input type="hidden" id="version" name="version" />
        <input type="hidden" id="uploadpath" name="uploadpath" value="<%=AppConstants.getUploadDirectory()%>"/>
        <div>
            <label>登录名:</label>
            <input type="text" id="loginName" name="loginName" maxLength="36"
                   class="easyui-validatebox textbox"
                   data-options="required:true,missingMessage:'请输入登录名.',validType:['minLength[1]']"/>
        </div>
        <div id="password_div">
            <label>密码:</label>
            <input type="password" id="password"
                   name="password" class="easyui-validatebox textbox" maxLength="36"
                   data-options="required:true,missingMessage:'请输入密码.',validType:['minLength[1]']">
        </div>
        <div id="repassword_div">
            <label>确认密码:</label>
            <input type="password" id="repassword"
                   name="repassword" class="easyui-validatebox textbox" required="true"
                   missingMessage="请再次填写密码." validType="equalTo['#password']"
                   invalidMessage="两次输入密码不匹配.">
        </div>
        <div>
            <label>姓名:</label>
            <input name="name" type="text" maxLength="6" class="easyui-validatebox textbox" data-options="validType:['CHS','length[2,6]']" />
        </div>
        <div>
            <label>头像:</label>
            <input id="photo" name="photo" readonly="readonly" value="${model.photo}" style="display: none" />
            <img id="photo_pre" src="" alt="头像" class="img-polaroid" style="width: 64px; height: 64px;">
            <input id="file" name="file"  multiple="true" >
        </div>
        <div>
            <label>性别:</label>
            <input id="sex" name="sex" style="width: 120px;" />
        </div>
        <div>
            <label>出生日期:</label>
            <input id="birthday" name="birthday" type="text" class="easyui-my97" style="height: 28px;" />
        </div>
        <div>
            <label>邮箱:</label>
            <input name="email" type="text" class="easyui-validatebox textbox" validType="email" maxLength="255" />
        </div>
        <div>
            <label>地址:</label>
            <input name="address" type="text" class="easyui-validatebox textbox" validType="legalInput" maxLength="255" />
        </div>
        <div>
            <label>电话:</label>
            <input name="tel" type="text" class="easyui-validatebox textbox" validType="phone">
        </div>
        <div>
            <label>手机号:</label>
            <input name="mobilephone" type="text" class="easyui-validatebox textbox" validType="mobile">
        </div>
        <div>
            <label>排序:</label>
            <input type="text" id="orderNo" name="orderNo" class="easyui-numberspinner"
                   data-options="min:1,max:99999999,size:9,maxlength:9,height:28" />
        </div>
        <div>
            <label >备注:</label>
            <input name="remark" maxLength="1000" class="easyui-textbox" data-options="multiline:true" style="width:260px;height:75px;">
        </div>
        <div>
            <label>状态:</label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="status" style="width: 20px;" value="0" /> 启用
            </label>
            <label style="text-align: left;width: 60px;">
                <input type="radio" name="status" style="width: 20px;" value="3" /> 停用
            </label>
        </div>
    </form>
</div>