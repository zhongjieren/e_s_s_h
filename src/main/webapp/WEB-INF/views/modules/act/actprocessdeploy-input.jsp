<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
        loadGroup();
	});
	//加载分组
	function loadGroup(){
        $('#category').combobox({
            url:'${ctxAdmin}/act/actprocesstype/combobox?selectType=select',
            multiple:false,//是否可多选
            editable:false,//是否可编辑
            height:28,
            width:120,
            groupField:'group',
            valueField:'value',
            textField:'text',
            onHidePanel:function(){
                //防止自关联
//                 if($('#id').val() != undefined && $(this).combobox('getValue') == $('#code').val()){
//                     $(this).combobox('setValue','');
//                 }
            }
        });
	}


</script>
<div>
	<form id="actprocessdeploy_form" class="dialog-form" method="post" enctype="multipart/form-data">
		<input type="hidden" id="id" name="id" />
        <!-- 用户版本控制字段 version -->
        <input type="hidden" id="version" name="version" />
	    <div>
			<label>流程类型:</label>
			<input id="category" name="category" />
		</div>
		<div>
			<label>流程文件:</label> 
			<input  id="file" name="file" style="width:300px"
				class="easyui-filebox"   placeholder="请选择文件..."
				data-options="prompt:'选择文件',buttonText:'&nbsp;选&nbsp;择&nbsp;',required:true,missingMessage:'请选择文件,支持文件格式:zip、bar、bpmn、bpmn20.xml.'" />
		</div> 
	</form>
</div>