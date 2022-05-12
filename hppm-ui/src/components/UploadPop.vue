<template>
  <v-dialog max-width="500" v-model="dialog">
    <template v-slot:activator="{ on, attrs }">
      <v-btn v-bind="attrs" v-on="on">
        <span>上传</span>
        <v-icon>mdi-cloud-upload</v-icon>
      </v-btn>
    </template>

    <v-card>
      <v-card-title>
        <h2>上传曲谱</h2>
      </v-card-title>

      <v-card-text>
        <v-form class="px-2">
          <v-container>
            <v-row align="center">
              <v-file-input
                      ref="uploadNotationImage"
                      v-model="selectedFiles"
                      multiple
                      small-chips
                      accept="image/png, image/jpeg, image/bmp"
                      placeholder="选择曲谱图片"
                      prepend-icon="mdi-camera"
                      @change="uploadNotationImage"
              ></v-file-input>
            </v-row>
            <v-row>
              <div id="filesId">
                <div   v-for="(item,index) in selectedFiles" :key="index">
                  <v-chip draggable>
                    {{item.name}}
                  </v-chip>
                </div>
              </div>
              
            </v-row>
            
            
            <v-row>
              <v-text-field label="歌曲名" v-model="songName"></v-text-field>
            </v-row>
            <v-row>
              <v-text-field label="歌手" v-model="artist"></v-text-field>
            </v-row>
            <v-row>
              <v-col cols="6">
                <v-select
                        v-model="notationCatgSelect"
                        :items="notationCatgItems"
                        item-text="category"
                        item-value="abbr"
                        label="Select"
                        persistent-hint
                        return-object
                        single-line
                ></v-select>
              </v-col>
              <v-col cols="6">
                <v-select
                        v-model="playCatgSelect"
                        :items="playCatgItems"
                        item-text="category"
                        item-value="abbr"
                        label="Select"
                        persistent-hint
                        return-object
                        single-line
                ></v-select>
              </v-col>
            </v-row>
          </v-container>

        </v-form>

        <v-card-actions>
          <v-btn :loading="submitLoading"  @click="submit">
            确认
          </v-btn>
          <v-btn @click="dialog = false">
            取消
          </v-btn>
        </v-card-actions>
      </v-card-text>



    </v-card>

  </v-dialog>

</template>

<script>
  import Sortable from 'sortablejs'
  
  export default {
    name: "UploadPop",

    data () {
      return {
        selectedFiles: undefined,
        notationPath: '',
        title: '',
        artist: '',
        
        notationCatgSelect : { category: '吉他谱', abbr: 'G'},
        notationCatgItems: [
          { category: '吉他谱', abbr: 'G' },
          { category: '尤克里里谱', abbr: 'U' },
        ],
        playCatgSelect : { category: '弹唱', abbr: 'TC'},
        playCatgItems: [
          { category: '弹唱', abbr: 'TC' },
          { category: '指弹', abbr: 'ZT' },
        ],
        
        dialog: false, // 弹出框
        submitLoading: false, // 提交的时候，确认按钮 loading 直到提交成功
      }
    },
    methods: {
      /**
       * 提交上传表单
       */ 
      submit () {
        this.submitLoading = true;

        console.log(this.songName, this.artist, this.notationCatgSelect, this.playCatgSelect, this.notationPicture);

        let notationDTO = {
          'songName' : this.songName,
          'artist' : this.artist,
          'notationCategory' : this.notationCatgSelect.abbr,
          'playCategory' : this.playCatgSelect.abbr,
          'notationPath' : this.notationPath,
        }

        console.log(notationDTO);
        
        this.$axios.post('/notation/add', notationDTO).then((response) => {
          console.log(response.data);
          this.submitLoading = false;
          this.dialog = false;
          this.$emit('notationSubmitted');
        });
      },

      /**
       * 当选择完曲谱图片后，先上传到后台临时文件
       * @param image
       */
      uploadNotationImage (files) {
        
        console.log(files)
        if (files && files.length > 0) {
          let formData = new FormData();
          for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
          }
          
          this.$axios.post('/notation/upload', formData).then((response) => {
              this.notationPath = '';
              for (let i = 0; i < response.data.data.length; i++) {
                this.notationPath += response.data.data[i] + ',';
              }
              console.log(this.notationPath)
          });
            
        } else {
          console.log('image is null');
        }

      },

      sortFiles() {
        var that = this
        var el = document.getElementById('filesId')
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

      
    },
    mounted () {
      this.sortFiles();
    },

    
  }
</script>

<style scoped>

</style>