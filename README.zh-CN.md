# 黎曼ζ函数零点计算器

[English](./README.md) | [中文](./README.zh-CN.md)

一个用 Java 实现的黎曼ζ函数非平凡零点在临界线上的计算与验证程序。

## 项目概述

本项目计算并展示黎曼ζ函数 ζ(s) 的前10个非平凡零点，这些零点位于复平面上的临界线 Re(s) = 1/2。这些零点在数论中具有深远意义，尤其与黎曼猜想密切相关。

## 算法关键逻辑

### 1. 狄利克雷级数（直接求和）
当 Re(s) > 1 时：
```
ζ(s) = Σ(1/n^s)，n 从 1 到 ∞
```
实现中使用截断级数，包含 5000 项。

### 2. 函数方程
当 Re(s) ≤ 1 时，代码使用函数方程：
```
ζ(s) = π^(s-1/2) * Γ((1-s)/2) / ζ(1-s)
```
通过将临界带内的 ζ(s) 值与 Re(s) > 1 的值关联起来进行计算。

### 3. Gamma 函数近似
Gamma 函数 Γ(z) 使用斯特林公式和递推关系进行近似，以在函数方程中实现精确计算。

## 项目结构

```
zeta-zeros/
├── pom.xml
├── src/main/java/com/zeta/
│   └── ZetaZeros.java
└── lib/
    └── commons-math3-3.6.1.jar
```

## 编译与运行

```bash
mvn compile exec:java -Dexec.mainClass="com.zeta.ZetaZeros"
```

或直接编译运行：
```bash
javac -cp "lib/*:src/main/java" src/main/java/com/zeta/ZetaZeros.java
java -cp "lib/*:src/main/java" com.zeta.ZetaZeros
```

## 输出

程序输出临界线上的前10个非平凡零点，并通过计算 ζ(1/2 + it) ≈ 0 进行验证。
