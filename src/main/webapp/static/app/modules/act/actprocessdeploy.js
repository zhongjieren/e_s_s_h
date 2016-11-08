var $actprocessdeploy_treegrid;
var $actprocessdeploy_form;
var $actprocessdeploy_dialog;
var $actprocessdeploy_search_form;
$(function() {
    //数据列表
    $actprocessdeploy_treegrid = $('#actprocessdeploy_treegrid').treegrid({
        url:ctxAdmin+'/act/actprocessdeploy/treegrid',
        fit:true,
        fitColumns:false,//自适应列宽
        striped:true,//显示条纹
        pageSize:20,//每页记录数
        singleSelect:false,//单选模式
        rownumbers:true,//显示行数
        nowrap : false,
        border : false,
        singleSelect:true,
        remoteSort:false,//是否通过远程服务器对数据排序
        sortName:'key',//默认排序字段
        sortOrder:'asc',//默认排序方式 'desc' 'asc'
        idField : 'id',
//        treeField:"name",
        frozenColumns:[[
            {field:'name',title:'流程名称',width:200}
        ]],
        columns:[ [
            {field : 'id',title : '主键',hidden : true,sortable:true,align : 'right',width : 80},
            {field : 'name',title : '流程名称',width : 200,sortable:true},
            {field : 'key',title : '流程主键',align : 'right',width : 80,sortable:true } ,
            {field : 'category',title : '流程分类',align : 'right',width : 80,sortable:true } ,
            {field : 'deploymentTime',title : '部署时间',align : 'right',width : 80,sortable:true } ,
            {field : 'description', title : '备注',width : 100 },
            { field :'resourceName',title : '操作',width : 100, formatter:function(value, row, index){
            	return '<a href="_black" onclick="edit('+row+')" >编辑</a>';
        	}}
        ] ],
        toolbar:[{
            text:'流程部署',
            iconCls:'easyui-icon-add',
            handler:function(){showDialog()}
        },'-',{
            text:'编辑',
            iconCls:'easyui-icon-edit',
            handler:function(){edit()}
        },'-',{
            text:'删除',
            iconCls:'easyui-icon-remove',
            handler:function(){del()}
        }],
        onContextMenu : function(e, row) {
            e.preventDefault();
            $(this).treegrid('select', row.code);
            $('#actprocessdeploy_menu').menu('show', {
                left : e.pageX,
                top : e.pageY
            });
        },
        onDblClickRow:function(row){
            edit(row);
        }
    }).datagrid('showTooltip');

//    loadGroupDictionaryType();
});

//设置排序默认值
function setSortValue() {
    $.get(ctxAdmin+'/act/actprocesstype/maxSort', function(data) {
        if (data.code == 1) {
            $('#orderNo').numberspinner('setValue',data.obj+1);
        }
    }, 'json');
}

function formInit(){
    $actprocessdeploy_form = $('#actprocessdeploy_form').form({
        url: ctxAdmin+'/act/actprocessdeploy/upload',
        onSubmit: function(param){
            $.messager.progress({
                title : '提示信息！',
                text : '数据处理中，请稍后....'
            });
            var isValid = $(this).form('validate');
            if (!isValid) {
                $.messager.progress('close');
            }
            return isValid;
        },
        success: function(data){
            $.messager.progress('close');
            var json = $.parseJSON(data);
            if (json.code ==1){
                $actprocessdeploy_dialog.dialog('destroy');//销毁对话框
                $actprocessdeploy_treegrid.treegrid('reload');//重新加载列表数据
                eu.showMsg(json.msg);//操作结果提示
            }else if(json.code == 2){
                $.messager.alert('提示信息！', json.msg, 'warning',function(){
                    if(json.obj){
                        $('#actprocessdeploy_form input[name="'+json.obj+'"]').focus();
                    }
                });
            }else {
                eu.showAlertMsg(json.msg,'error');
            }
        },
        onLoadSuccess:function(data){
//            if(data && data._parentId){
//                //$('#_parentId')是弹出-input页面的对象 代表所属分组
//                $('#category').combobox('setValue',data._parentId);
//            }
//            //如果存在子节点 即为分组节点
//            if(data.children){
//                //将类别分组设置为只读 $('#_parentId')是弹出页面的对象
//                $('#category').combobox('disable',true);
//            }
        }
    });
}
//显示弹出窗口 新增：row为空 编辑:row有值
function showDialog(row){
    var inputUrl = ctxAdmin+"/act/actprocessdeploy/input";
    if(row != undefined && row.id){
        inputUrl = inputUrl+"?id="+row.id;
    }

    //弹出对话窗口
    $actprocessdeploy_dialog = $('<div/>').dialog({
        title:'流程部署操作',
        top:20,
        width : 500,
        height:360,
        modal : true,
        maximizable:true,
        href : inputUrl,
        buttons : [ {
            text : '保存',
            iconCls : 'easyui-icon-save',
            handler : function() {
                $actprocessdeploy_form.submit();
            }
        },{
            text : '关闭',
            iconCls : 'easyui-icon-cancel',
            handler : function() {
                $actprocessdeploy_dialog.dialog('destroy');
            }
        }],
        onClose : function() {
            $actprocessdeploy_dialog.dialog('destroy');
        },
        onLoad:function(){
            formInit();
            setSortValue();
            var initFormData = {};
            $actprocessdeploy_form.form('load',initFormData );
        }
    });
}

//编辑
function editButton(row){
	if(row == undefined){ 
        return "";
    }
	return '<a href="javasrcipt:void(0);" onclick="edit('+row+')" >编辑</a>';
}


//编辑
function edit(row){
    if(row != undefined){
        showDialog(row);
        return;
    }
    //选中的所有行
    var rows = $actprocessdeploy_treegrid.treegrid('getSelections');
    //选中的行（第一次选择的行）
    var row = $actprocessdeploy_treegrid.treegrid('getSelected');
    if (row){
        if(rows.length>1){
            row = rows[rows.length-1];
            eu.showMsg("您选择了多个操作对象，默认操作第一次被选中的记录！");
        }
        showDialog(row);
    }else{
        eu.showMsg("您未选择任何操作对象，请选择一行数据！");
    }
}

//删除
function del(){
    var rows = $actprocessdeploy_treegrid.treegrid('getSelections');

    if(rows.length >0){
        $.messager.confirm('确认提示！','您确定要删除选中的所有行(如果存在子节点，子节点也一起会被删除)？',function(r){
            if (r){
                var ids = new Array();
                $.each(rows,function(i,row){
                    ids[i] = row.id;
                });
                $.ajax({
                    url:ctxAdmin+'/act/actprocesstype/remove',
                    type:'post',
                    data: {ids:ids},
                    traditional:true,
                    dataType:'json',
                    success:function(data) {
                        if (data.code==1){
                            $actprocessdeploy_treegrid.treegrid('load');	// reload the user data
                            eu.showMsg(data.msg);//操作结果提示
                        } else {
                            eu.showAlertMsg(data.msg,'error');
                        }
                    }
                });
            }
        });
    }else{
        eu.showMsg("您未选择任何操作对象，请选择一行数据！");
    }
}

function loadGroupDictionaryType(){
    $('#filter_EQS_groupactprocessdeploy__code').combobox({
        url:ctxAdmin+'/act/actprocesstype/group_combobox?selectType=all',
        multiple:false,//是否可多选
        editable:false,//是否可编辑
        width:120
    });
}