# 🧮 Material Design Calculator

A beautiful, modern calculator app built with **Material Design 3** principles, featuring advanced mathematical functions and smooth animations.

## ✨ Features

### 🎨 **Modern UI Design**
- **Material Design 3** components and theming
- **Light & Dark Theme** support with automatic switching
- **Responsive Layout** optimized for phones and tablets
- **Smooth Animations** with ripple effects and elevation changes
- **Beautiful Typography** with proper font weights and spacing

### 🔢 **Mathematical Functions**
- **Basic Operations**: Addition (+), Subtraction (−), Multiplication (×), Division (÷)
- **Advanced Functions**: Square Root (√), Power (x²), Percentage (%)
- **Parentheses Support**: Complex expressions with proper precedence
- **Decimal Numbers**: Full decimal point support
- **Real-time Preview**: Live calculation as you type

### 📱 **User Experience**
- **Single Input Field**: Shows both expression and result
- **Smart Input Handling**: Prevents invalid expressions
- **Error Management**: Clear error messages with auto-recovery
- **Accessibility**: Full screen reader support with content descriptions
- **Responsive Design**: Works perfectly in portrait and landscape modes

## 🏗️ **Technical Architecture**

### **Expression Parser**
- **Infix to Postfix Conversion**: Proper mathematical operator precedence
- **Tokenization**: Smart parsing of mathematical expressions
- **Error Handling**: Robust validation and error recovery
- **BigDecimal Precision**: Accurate calculations for large numbers

### **UI Components**
- **MaterialCardView**: Elevated containers with rounded corners
- **MaterialButton**: Custom styled buttons with ripple effects
- **TextInputLayout**: Modern input fields with animations
- **GridLayout**: Responsive button grid for different screen sizes

### **Animation System**
- **Button Press Animations**: Scale and elevation changes (150ms)
- **Result Transitions**: Smooth fade in/out effects
- **Ripple Effects**: Custom ripple colors for different button types
- **State Changes**: Animated color transitions for results

## 🎯 **Design Specifications**

### **Color Palette**
- **Primary**: Blue (#1976D2) for operators and results
- **Secondary**: Orange (#FF9800) for functions
- **Error**: Red (#F44336) for clear/delete buttons
- **Surface**: White/Dark gray for backgrounds
- **171 Total Colors** for comprehensive theming

### **Typography**
- **Display**: 48sp for results (36sp landscape)
- **Expression**: 18sp for input expressions
- **Buttons**: 20sp-24sp for button labels
- **Sans-serif Family** with proper font weights

### **Spacing System**
- **4dp Base Unit**: Consistent spacing throughout
- **Button Margins**: 4dp (3dp landscape)
- **Card Padding**: 16-24dp
- **Corner Radius**: 12-24dp for modern aesthetics

## 📂 **Project Structure**

```
app/src/main/
├── java/com/example/calculator/
│   └── MainActivity.java          # Main calculator logic (739 lines)
├── res/
│   ├── layout/
│   │   ├── activity_main.xml      # Portrait layout (647+ lines)
│   │   └── layout-land/
│   │       └── activity_main.xml  # Landscape layout (500+ lines)
│   ├── values/
│   │   ├── colors.xml             # Color palette (171 colors)
│   │   ├── themes.xml             # Material Design 3 themes
│   │   ├── strings.xml            # Localized strings
│   │   └── dimens.xml             # Dimension resources
│   ├── values-night/
│   │   ├── colors.xml             # Dark theme colors
│   │   └── themes.xml             # Dark theme overrides
│   └── drawable/
│       ├── button_number.xml      # Number button styling
│       ├── button_operator.xml    # Operator button styling
│       ├── button_function.xml    # Function button styling
│       ├── button_equals.xml      # Equals button with gradient
│       ├── button_clear.xml       # Clear/delete button styling
│       ├── input_background.xml   # Input field background
│       ├── calculator_background.xml # App background gradient
│       └── display_background.xml # Display container styling
```

## 🚀 **Getting Started**

### **Prerequisites**
- Android Studio Arctic Fox or later
- Android SDK API 24+ (Android 7.0)
- Java 11 or later

### **Installation**
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Run on device or emulator

### **Dependencies**
```gradle
implementation 'com.google.android.material:material:1.10.0'
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'
implementation 'androidx.dynamicanimation:dynamicanimation:1.0.0'
```

## 🎨 **Customization**

### **Themes**
- Modify `colors.xml` for custom color schemes
- Update `themes.xml` for different Material Design variants
- Add new drawable resources for custom button styles

### **Layout**
- Portrait: 6×4 button grid in `activity_main.xml`
- Landscape: 4×6 button grid in `layout-land/activity_main.xml`
- Tablet: Automatically scales with responsive design

### **Functions**
- Add new mathematical functions in `MainActivity.java`
- Extend the expression parser for additional operators
- Create custom button styles in drawable resources

## 🧪 **Testing**

### **Mathematical Accuracy**
- ✅ Basic arithmetic operations
- ✅ Order of operations (PEMDAS)
- ✅ Decimal calculations
- ✅ Advanced functions (√, x², %)
- ✅ Parentheses and complex expressions
- ✅ Error handling (division by zero, invalid input)

### **UI/UX Testing**
- ✅ Portrait and landscape orientations
- ✅ Light and dark themes
- ✅ Button animations and ripple effects
- ✅ Accessibility with screen readers
- ✅ Different screen sizes and densities

## 📱 **Screenshots**

*Beautiful Material Design 3 interface with smooth animations and professional styling*

## 🤝 **Contributing**

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 **License**

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 **Acknowledgments**

- **Material Design 3** guidelines by Google
- **Android Jetpack** components
- **Mathematical expression parsing** algorithms
- **Modern Android development** best practices

---

**Built with ❤️ using Material Design 3 and modern Android development practices**
