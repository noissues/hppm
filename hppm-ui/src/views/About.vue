<template>
  <div>
    <div class="flex" id="items">
      <div  v-for="(item,index) in files" :key="index">
        <!--      <p>{{item.name}}</p>-->
<!--        <v-badge-->
<!--                color="green"-->
<!--              -->
<!--        >-->
          <v-chip draggable>
            {{item.name}}
          </v-chip>
<!--        </v-badge>-->
        
      </div>
    </div>

    <v-file-input ref="dragSelect"
            v-model="files"
            placeholder="Upload your documents"
            label="File input"
            multiple
            prepend-icon="mdi-paperclip"
                  @change="test1"
    >
      <template v-slot:selection="{ text }">
        <v-chip
                small
                label
                color="primary"
                draggable
        >
          {{ text }}
        </v-chip>
      </template>
    </v-file-input>
  </div>
</template>
<style scope>


</style>

<script>
  import Sortable from 'sortablejs'
  export default {
    name: 'operationsAdd',
    data () {
      return {
        value: [
          { name: 11111 },
          { name: 22222 },
          { name: 33333 },
          { name: 44444 }
        ],
        files: [],
      }
    },
    methods: {
      test() {
        var that = this
        var el = document.getElementById('items')
        // 常用
        new Sortable(el, {
          onEnd: function (evt) {
            // 获取排序之后的data数据
            that.files.splice(evt.newIndex, 0, that.files.splice(evt.oldIndex, 1)[0])
            var newArray = that.files.slice(0)
            that.files = []
            that.$nextTick(function () {
              that.files = newArray
              console.log(that.files)
            })
          }
        })
      },
      test1() {
        var that = this
        const ele = this.$refs.dragSelect.$el.querySelectorAll('v-file-input__text > span')[0];
        // 常用
        this.sortable =  Sortable.create(ele, {
          onEnd: function (evt) {
            // 获取排序之后的data数据
            that.files.splice(evt.newIndex, 0, that.files.splice(evt.oldIndex, 1)[0])
            // var newArray = that.value.slice(0)
            // that.files = []
            // that.$nextTick(function () {
            //   that.files = newArray
            //   console.log(that.files)
            // })
          }
        })
      }
    },
    mounted () {
      this.test();
      this.test1();
    }
  }
</script>
