<template>
    <div>
        <div v-for="(item, index) in list" :key="index">
            <div class="item-name">
                <span>{{item.name}}:</span>
            </div>
            <div v-if="item.childCategory" class="children-item">
                <list :list="item.childCategory" style="margin-left:20px"></list>
            </div>
        </div>
    </div>
</template>
<script>
export default {
  name: "List",
  props: {
    list: Array
  }
};
</script>