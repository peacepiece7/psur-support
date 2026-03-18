# Form Builder Shared SDD

## 1. Purpose
- This document defines the shared schema-driven form contract for the project.
- The immediate target is to evolve the current hand-built application forms into Form.io-driven forms.

## 2. Current Frontend Form Stack
- `vee-validate`
- `useForm`
- `useFormContext`
- `FormField`
- `TextField`
- `Textarea`
- `Select`
- `SelectV2`
- `DatePicker`
- `TimePicker`
- `FileUpload`
- `FormSingleFileUpload`
- `FormMultiFileUpload`
- `FormAddressInputs`
- `PhoneNumberInput`
- `IDNumberInput`
- `SelectionGroup`
- `ToggleGroup`

## 3. Schema-Driven Goal
- Use a form schema as the first-class contract for:
  - rendering fields
  - field ordering
  - validation
  - option loading
  - payload mapping
  - draft restore
- Future preferred schema source: Form.io schema

## 4. Shared Schema Concepts
- `formKey`
- `formVersion`
- `pages` or `steps`
- `sections`
- `fields`
- `componentType`
- `binding`
- `validation`
- `optionSource`
- `visibilityRule`
- `readonlyRule`
- `layout`

## 5. Shared Component Mapping Contract
- `text` -> `TextField`
- `textarea` -> `Textarea`
- `select` -> `SelectV2`
- `date` -> `DatePicker`
- `time` -> `TimePicker`
- `single-file` -> `FormSingleFileUpload`
- `multi-file` -> `FormMultiFileUpload`
- `address` -> `FormAddressInputs`
- `phone` -> `PhoneNumberInput`
- `id-number` -> `IDNumberInput`

## 6. Shared Binding Contract
- UI field names do not always match backend DTO names
- A schema-driven mapper layer is required
- mapper must support:
  - direct value
  - nested value extraction
  - array mapping
  - default fallback

## 7. Shared Builder Responsibilities
- author schema
- preview schema
- publish schema
- version schema
- render schema
- validate schema
- map schema data to backend request

## 8. Current Gaps
- no actual Form.io integration yet
- no persisted schema registry yet
- no centralized payload mapper
- no centralized renderer for current page forms
