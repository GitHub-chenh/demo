<script setup lang="ts">
import { ref } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
const username = ref('')
const password = ref('')

const onSubmit = ( username:string )=>{
    if (username) {
        ElMessage({
            message: 'Login Success!',
            type: 'success',
        })
    }else{
        ElMessage({
            message: 'Login failed!',
            type: 'error',
        })
    }

}
</script>

<template>
    <el-container>
      <el-header><h1>Login</h1></el-header>
      <el-main>
        <el-form :model="form" label-width="auto" style="max-width: 600px">
            <el-form-item label="用户名">
                <el-input v-model="username" class="username" placeholder="username" clearable :prefix-icon="UserFilled"/>
            </el-form-item>
            <el-form-item label="密码">
                <el-input v-model="password" class="password" placeholder="password" clearable :prefix-icon="UserFilled"/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" :plain="true" @click="onSubmit(username)">Login</el-button>
                <el-button>Register</el-button>
            </el-form-item>
        </el-form>
      </el-main>
    </el-container>
</template>

<style scoped>
.username{
    width: 240px
}
.password{
    width: 240px
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}
.el-header{
    text-align: center;
}
</style>