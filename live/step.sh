#!/bin/sh
# ライブの作業ファイルを、指定したステップの「緑」の状態にする。0 は空から始める状態。
#   ./step.sh 0   … 空の 2 ファイル（本番開始前）
#   ./step.sh 4   … docs/souther/live/step4-*/ の内容
set -eu
cd "$(dirname "$0")"
n="${1:-0}"
if [ "$n" = 0 ]; then
    printf '' > 労働時間帯分割.sou
    printf '' > 労働時間帯分割.examples.sou
    rm -rf out
    echo "空にしました"
    exit 0
fi
dir=$(ls -d ../docs/souther/live/step"$n"-*/ 2>/dev/null | head -1)
[ -n "$dir" ] || { echo "step$n はありません" >&2; exit 1; }
cp "$dir"労働時間帯分割.sou .
if [ -f "$dir"労働時間帯分割.examples.sou ]; then
    cp "$dir"労働時間帯分割.examples.sou .
else
    printf '' > 労働時間帯分割.examples.sou
fi
rm -rf out
echo "$dir の状態にしました"
