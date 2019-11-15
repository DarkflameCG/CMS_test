<template>
  <div>
    <!-- 按钮区域 -->
    <div class="btns">
      <el-button type="primary" size="small" @click="toAdd">新增</el-button>
      <el-button type="danger" size="small" @click="toBatchDelete">批量删除</el-button>
    </div>
    <!-- 表格区域 -->
    {{ids}}
    <el-table :data="categorys" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"> </el-table-column>
      <el-table-column prop="id" label="编号"></el-table-column>
      <el-table-column prop="name" label="栏目名称"></el-table-column>
      <el-table-column prop="description" label="栏目描述"></el-table-column>
      <el-table-column prop="parentId" label="父栏目"></el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        width="150">
        <template slot-scope="scope">
          <el-button @click="toDelete(scope.row.id)" type="text" size="small">删除</el-button>
          <el-button @click="toEdit(scope.row)" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 模态框 -->
    <el-dialog title="新增栏目信息" :visible.sync="dialogFormVisible">
      {{form}}
      <el-form :model="form">
        <el-form-item label="栏目名称" label-width="80px">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="栏目描述" label-width="80px">
          <el-input type="textarea" v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="submitHandler">确 定</el-button>
      </div>
    </el-dialog>
    <!-- /模态框 -->
  </div>
</template>
<script>
import request from '@/utils/request'
import qs from 'querystring'
export default {
  // 为模板提供数据
  data(){
    return {
      dialogFormVisible:false,
      form:{},
      categorys:[],
      ids:[]
    }
  },
  // 生命周期
  created(){
    this.reloadData();
  },
  // 方法，事件处理函数
  methods:{
    handleSelectionChange(val){
      this.ids = val.map(item => item.id);
    },
    submitHandler(){
      request.request({
        url:'http://127.0.0.1:8888/category/saveOrUpdate',
        method:'post',
        headers:{
          'Content-Type':'application/x-www-form-urlencoded'
        },
        data:qs.stringify(this.form)
      })
      .then(response=>{
        // 提示信息
        this.$message({
          message:response.message,
          type:"success"
        })
        // 关闭模态框
        this.dialogFormVisible = false;
        // 重载数据
        this.reloadData();
        
      })
    },
    reloadData(){
      // 调用ajax查询所有文章数据
      let url = "http://127.0.0.1:8888/category/findAll"
      request
      .get(url)
      .then(result => {
        this.categorys = result.data;
      })
    },
    toEdit(record){
      this.dialogFormVisible = true;
      // 为form绑定要修改的值
      this.form = record;
    },
    // 批量删除
    toBatchDelete(){
      let url = "http://127.0.0.1:8888/category/batchDelete"
      request
      .post(url,{ids:this.ids})
      .then(response=>{
        this.$message({
          message:response.message,
          type:"success"
        })
      })
    },
    toDelete(id){
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 交互
        let url ="http://localhost:8888/category/deleteById"
        request.get(url,{params:{id}})
        .then(response =>{
          // 通知
          this.$message({
            message:response.message,
            type:"success"
          })
          // 重载数据
          this.reloadData();
        })
      })
    },
    toAdd(){
      this.form = {};
      // 跳转
      this.dialogFormVisible = true;
    }
  }
}
</script>
<style scoped>

</style>