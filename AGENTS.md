# TFC Hammer 仕様

TerraFirmaCraftの金属体系へ3x3採掘のSledgehammerとExcavatorを追加するMinecraft 1.21.1 / NeoForge向けModである。

## 共通開発ルール

- 仕様ファイル名は大文字の `AGENTS.md` に統一する。
- 本書は現在維持すべき仕様を記録し、挙動、対応バージョン、依存関係、ID、登録内容、有効化条件、生成規則、検証手順の変更と同時に更新する。
- READMEは利用者向けの短い概要とbuild入口に絞り、詳細仕様や使用例を重複掲載しない。
- 現在値は `gradle.properties`、Gradle設定、Mod metadata、実装コード、同梱dataを正本とし、別バージョンの記憶ではなく対象版の公式ソース、依存ソース、実JAR/dataで確認する。
- 公開API、registry、tag、dataを優先し、client専用クラスをcommon/server側から参照しない。任意依存のクラスは、そのModがない通常ロード経路から参照しない。
- 公開済みIDはworld/data pack互換性を優先する。依存JAR、展開物、一時解析物は `.tmp/`、再利用する処理は `tools/` に置き、BOMなしUTF-8のJSONを使う。
- `.gradle/`、`build/`、`run/`、IDE metadata、依存JARを変更対象に含めず、無関係な既存差分を編集しない。
- 変更前に既存の登録、命名、resource配置、生成方法を確認し、依頼外のrename、format変更、依存・version更新を混ぜない。
- 通常は `./gradlew compileJava`、完了時は `./gradlew build` を実行する。data変更時は全JSONをparseし、optional連携は対象Modの有無、採掘変更はdrop・耐久・保護eventを確認する。
- Minecraftクライアントはランタイム確認が必要な変更または明示依頼時だけ起動し、未実施の検証は理由と範囲を報告する。

## 基本情報

- Mod IDは `tfchammer`、Java packageは `net.claustra01.tfchammer`。
- Minecraftは `1.21.1`、NeoForgeは `21.1.235`、Javaは `21`。
- TFCを必須依存とし、TFCM（`tfcm`）とTFC Metal Tools向けdataは任意連携として扱う。正確なversionとmetadataは `gradle.properties` と `src/main/templates/META-INF/neoforge.mods.toml` を正とする。

## ItemとID

- TFCでpickaxeを持つ金属へ `tfchammer:metal/sledgehammer/<metal>` と `sledgehammer_head/<metal>` を登録する。
- TFCでshovelを持つ金属へ `tfchammer:metal/excavator/<metal>` と `excavator_head/<metal>` を登録する。
- TFCM導入時だけ `invar`、`titanium`、`tungsten_steel`、`netherite` の両toolとheadを登録し、TFCM本体のtool tierを使用する。
- ceramic moldはunfired/firedのSledgehammer Head MoldとExcavator Head Moldを維持し、knapping、heating、casting経路をTFC金属体系へ接続する。
- tool、head、moldは既存のCommon/TFC tag、item heat、item size、tool rack、物理damage分類を維持する。

## 3x3採掘

- Sledgehammerはpickaxe対象、Excavatorはshovel対象だけを採掘し、採掘面に平行な3x3から起点blockを除く最大8blockを追加破壊する。
- 起点blockに適正tool判定が通る場合だけ範囲採掘し、追加blockも個別に適正tool判定する。採掘速度は元tierの40%とする。
- 追加破壊ごとにNeoForge `BlockEvent.BreakEvent` を発火し、cancelされたblockは変更しない。
- survivalでは追加blockごとに正規dropを生成して耐久を1消費し、耐久切れで停止する。creativeでは追加dropと耐久消費を行わない。
- `LAYERS` propertyを持つblockは一層ずつ減らし、最後の一層だけblockを除去する。
- block entityを持つblockのdrop計算ではblock entityを渡し、保護Modやloot挙動を迂回しない。

## Resource規則

- recipeは `data/tfchammer/recipe`、TFC item heat/sizeとtag追加は対象namespaceの `data/tfc`、Common tagは `data/c` に置く。
- 通常組立recipeとTFC Metal Tools互換recipeの条件を混同せず、対象Modの有無で一方だけが有効になる状態を維持する。
- TFCMのJARをrepository rootへ置かない。ローカル参照が必要なら `.tmp/` またはGradle dependencyとして扱う。
