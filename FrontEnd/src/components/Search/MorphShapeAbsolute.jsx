import React from 'react';
import { motion } from 'framer-motion';
import './MorphShapeAbsolute.css';
import CountrySelectorModal from './CountrySelectorModal';

const MorphShapeAbsolute = ({ toggled, onSelect }) => {
  // const [toggled, setToggled] = useState(false);

  return (
    <div
      className="shape-wrapper"
      // onClick={() => setToggled(prev => !prev)}
    >
      <motion.div
        className="shape"
        initial={{
          width: 10,
          height: 10,
          borderRadius: '50%',
          backgroundColor: '#4f46e5',
        }}
        animate={{
          width: toggled ? 1200 : 10,
          height: toggled ? '80vh' : 10,
          borderRadius: toggled ? '20px' : '50%',
          opacity: toggled ? 0.9 : 1,
          backgroundColor: 'rgba(40, 42, 50)',
          border: toggled? '1px solid rgba(255, 255, 255, 0.1)' : '',
        }}
        transition={{
          type: 'spring',
          stiffness: 200,
          damping: 20,
        }}
        onClick={ (e) => e.stopPropagation()}
      >
        <CountrySelectorModal
          show={toggled}
          onSelect={onSelect}
        />
      </motion.div>
    </div>
  );
};

export default MorphShapeAbsolute;
