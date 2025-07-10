// src/components/Common/DeleteButton.jsx
import React from 'react';
import PropTypes from 'prop-types';
import { Trash2 } from 'lucide-react';
import './CommentDeleteButton.css';

const DeleteButton = ({
    onDelete,
    disabled = false,
    className = '',
    children = '삭제',
    }) => {
        return (
            <button
                type="button"
                onClick={disabled ? undefined : onDelete}
                disabled={disabled}
                className={`delete-button ${className}`}
            >
                <Trash2 size={16} className="delete-button__icon" />
                {children}
            </button>
        );
    };

    DeleteButton.propTypes = {
    onDelete: PropTypes.func.isRequired,
    disabled: PropTypes.bool,
    className: PropTypes.string,
    children: PropTypes.node,
    };

export default DeleteButton;
