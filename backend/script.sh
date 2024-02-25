#!/bin/bash

# 数组包含要克隆的仓库地址
repos=(
    "git@github.com:openshopio/openshop.io-android"
)

# 指定克隆目录
clone_dir="./data"
output_file="repo_names.txt"


# 创建克隆目录
mkdir -p "$clone_dir"
# 清空或创建输出文件
echo -n > "$output_file"

# 循环克隆每个仓库
for repo in "${repos[@]}"; do
    # 提取仓库名称
    repo_name=$(basename "$repo" .git)


    # 克隆仓库
    git clone "$repo" "$clone_dir/$repo_name"

    # 检查克隆是否成功
    if [ $? -eq 0 ]; then
        echo "克隆 $repo_name 成功"
        # 将 repo_name 追加到输出文件，并用逗号分隔
        echo -n "\"$repo_name\"," >> "$output_file"
    else
        echo "克隆 $repo_name 失败"
    fi
done

# 删除输出文件末尾的逗号
sed -i 's/,$//' "$output_file"

echo "克隆完成"
echo "仓库名称已输出到 $output_file"
