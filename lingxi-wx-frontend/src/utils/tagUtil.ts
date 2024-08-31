export const tagStrToList = (tagStr: string) => {
    if (!tagStr) return [];
    return JSON.parse(tagStr);
};

/**
 * 标签数组转换树数组
 */
export const tagListToTreeList = (tagList: any[]) => {
    let arr: any[] = [];
    let other: any = {
        id: -1,
        text: '其他'
    };
    tagList.forEach((tag: any) => {
        if (tag.isParent == 1) {
            arr.push({
                id: tag.id,
                text: tag.tagName
            });
        } else {
            let index = arr.findIndex((item) => item.id == tag.parentId);
            if (index == -1) {
                if (other.hasOwnProperty('children')) {
                    other.children.push({ id: tag.id, text: tag.tagName });
                } else {
                    other.children = [{ id: tag.id, text: tag.tagName }];
                }
            } else {
                if (arr[index].hasOwnProperty('children')) {
                    arr[index].children.push({ id: tag.id, text: tag.tagName });
                } else {
                    arr[index].children = [{ id: tag.id, text: tag.tagName }];
                }
            }
        }
    });
    if (other.hasOwnProperty('children')) {
        arr.push(other);
    }
    return arr;
};
