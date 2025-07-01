// src/components/FancySwitch.jsx
import React, { useState, useEffect, useCallback, useRef, useMemo } from 'react';

function CustomFancySwitch({
  options,
  valueKey = 'value',
  labelKey = 'label',
  disabledKey = 'disabled',
  value,
  onChange,
  radioClassName,
  highlighterClassName,
  highlighterIncludeMargin = false,
  highlighterStyle: customHighlighterStyle = {},
  disabledOptions = [],
  renderOption,
  ...props
}) {
  const containerRef = useRef(null);
  const radioRefs = useRef([]);
  const [hoveredIndex, setHoveredIndex] = useState(null);

  const getOptionValue = useCallback(
    option => (typeof option !== 'object' ? option : option[valueKey]),
    [valueKey]
  );
  const getOptionLabel = useCallback(
    option => (typeof option !== 'object' ? String(option) : String(option[labelKey])),
    [labelKey]
  );
  const isOptionDisabled = useCallback(
    option => {
      const val = getOptionValue(option);
      if (disabledOptions.includes(val)) return true;
      return typeof option === 'object' && option[disabledKey];
    },
    [disabledOptions, getOptionValue, disabledKey]
  );
  const memoizedOptions = useMemo(
    () =>
      options.map(option => {
        const label = getOptionLabel(option);
        const val = getOptionValue(option);
        const disabled = isOptionDisabled(option);
        return { ...(typeof option === 'object' ? option : {}), label, value: val, disabled };
      }),
    [options, getOptionValue, getOptionLabel, isOptionDisabled]
  );

  // 초기에는 value에 매핑되는 인덱스, 없으면 -1
  const [activeIndex, setActiveIndex] = useState(() => {
    const idx = memoizedOptions.findIndex(o => o.value === value);
    return idx;
  });

  // value 변화 시 activeIndex 동기화 (매핑 안되면 -1)
  useEffect(() => {
    const idx = memoizedOptions.findIndex(o => o.value === value);
    if (idx !== activeIndex) setActiveIndex(idx);
  }, [value, memoizedOptions, activeIndex]);

  // hover 우선, 없으면 activeIndex
  const highlightIndex = hoveredIndex !== null ? hoveredIndex : activeIndex;
  const [highlighterStyle, setHighlighterStyle] = useState({});

  // 하이라이터 위치 업데이트 (out-of-range 슬라이드 처리 포함)
  const updateToggle = useCallback(() => {
    const count = options.length;
    const idx = highlightIndex;
    const container = containerRef.current;
    if (!container) return;

    const contRect = container.getBoundingClientRect();
    const contStyle = window.getComputedStyle(container);
    const padLeft = parseFloat(contStyle.paddingLeft) || 0;
    const padTop = parseFloat(contStyle.paddingTop) || 0;
    const borderLeft = parseFloat(contStyle.borderLeftWidth) || 0;
    const borderTop = parseFloat(contStyle.borderTopWidth) || 0;
    const slotWidth = contRect.width / count;

    if (idx >= 0 && idx < count) {
      const sel = radioRefs.current[idx];
      if (sel) {
        const selRect = sel.getBoundingClientRect();
        const selStyle = window.getComputedStyle(sel);
        const marginLeft = parseFloat(selStyle.marginLeft) || 0;
        const marginRight = parseFloat(selStyle.marginRight) || 0;
        const marginTop = parseFloat(selStyle.marginTop) || 0;

        const x =
          selRect.left - contRect.left - padLeft - borderLeft - (highlighterIncludeMargin ? marginLeft : 0);
        const y =
          selRect.top - contRect.top - padTop - borderTop - (highlighterIncludeMargin ? marginTop : 0);

        setHighlighterStyle({
          height: selRect.height,
          width: selRect.width + (highlighterIncludeMargin ? marginLeft + marginRight : 0),
          transform: `translate(${x}px, ${y}px)`
        });
      }
    } else {
      // nav에 없는 경로: 왼쪽(-) 또는 오른쪽(+)으로 슬라이드
      const x = idx < 0 ? -slotWidth : contRect.width;
      setHighlighterStyle({
        transform: `translate(${x}px, 0)`
      });
    }
  }, [highlightIndex, options.length, highlighterIncludeMargin]);

  const handleChange = useCallback(
    idx => {
      if (!memoizedOptions[idx].disabled) {
        const el = radioRefs.current[idx];
        if (el && el.focus) el.focus();
        setActiveIndex(idx);
        onChange && onChange(memoizedOptions[idx].value);
      }
    },
    [memoizedOptions, onChange]
  );

  const renderOptionContent = useCallback(
    (option, idx) => {
      const isSelected = idx === activeIndex;
      const propsBase = {
        ref: el => (radioRefs.current[idx] = el),
        role: 'radio',
        'aria-checked': isSelected,
        tabIndex: isSelected && !option.disabled ? 0 : -1,
        onClick: () => handleChange(idx),
        onMouseEnter: () => setHoveredIndex(idx),
        onMouseLeave: () => setHoveredIndex(null),
        className: radioClassName,
        ...(isSelected && { 'data-checked': true }),
        ...(option.disabled && { 'aria-disabled': true, 'data-disabled': true }),
        'aria-label': `${option.label} option`
      };
      return renderOption
        ? renderOption({ option, isSelected, getOptionProps: () => propsBase })
        : <div {...propsBase}>{option.label}</div>;
    },
    [activeIndex, handleChange, radioClassName, renderOption]
  );

  useEffect(() => updateToggle(), [updateToggle]);
  useEffect(() => {
    const ro = new ResizeObserver(updateToggle);
    if (containerRef.current) ro.observe(containerRef.current);
    return () => ro.disconnect();
  }, [updateToggle]);

  return (
    <div
      role="radiogroup"
      aria-label="Fancy switch options"
      ref={containerRef}
      onKeyDown={e => {
        props.onKeyDown && props.onKeyDown(e);
        if (!e.defaultPrevented) {
          let newIdx;
          if (['ArrowRight','ArrowDown'].includes(e.key)) {
            e.preventDefault(); newIdx = (highlightIndex + 1) % options.length; handleChange(newIdx);
          } else if (['ArrowLeft','ArrowUp'].includes(e.key)) {
            e.preventDefault(); newIdx = (highlightIndex - 1 + options.length) % options.length; handleChange(newIdx);
          }
        }
      }}
      {...props}
    >
      <div
        className={highlighterClassName}
        style={{
          position: 'absolute',
          transition: 'all 300ms cubic-bezier(0.4,0,0.2,1)',
          ...highlighterStyle,
          ...customHighlighterStyle
        }}
        aria-hidden="true"
        data-highlighter
      />
      {memoizedOptions.map((opt, idx) => (
        <React.Fragment key={opt.value.toString()}>
          {renderOptionContent(opt, idx)}
        </React.Fragment>
      ))}
      <div aria-live="polite" style={{ position: 'absolute', width: '1px', height: '1px', padding: 0, margin: '-1px', overflow: 'hidden', clip: 'rect(0,0,0,0)', whiteSpace: 'nowrap', borderWidth: 0 }}>
        {memoizedOptions[highlightIndex]?.label} selected
      </div>
    </div>
  );
}

export default CustomFancySwitch;
