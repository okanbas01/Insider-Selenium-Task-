# Insider Careers QA Automation
This guide outlines the steps for building and running tests within the **Insider Careers QA Automation** project (Selenium + Java + TestNG).

---

## 🧰 Tech Stack
- Java 17+
- Maven 3.8+
- Selenium WebDriver 4.x
- TestNG 7.x
- (Optional) SLF4J + simple/logback
- (Optional) Allure / Extent Reports
- (Optional) GitHub Actions CI

---

## ✅ Features
- Page Object Model (POM) architecture
- Reusable **BaseTest / BasePage** foundation
- Smart explicit waits
- Stable locators (CSS/XPath with normalization)
- Parametric test runs (Maven/TestNG)
- Headless mode support
- (Optional) Allure/Extent reporting
- CI example (GitHub Actions)

---

## 🔧 Getting Started

### 1) Prerequisites
- **Java 17+** installed (`java -version`)
- **Maven 3.8+** installed (`mvn -v`)
- **Git** installed (`git --version`)
- **Google Chrome** or **Chrome for Testing** (see *Troubleshooting* for version compatibility notes)

### 2) Setup
```bash
git clone https://github.com/okanbas01/okan-bas-careers-qa-automation.git
cd </insider-careers-qa-automation>
mvn clean install -DskipTests
