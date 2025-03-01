import Header from "@/components/Header";
import { Button } from "@/components/shadcdn/ui/button";
import { MdArrowBack } from "react-icons/md";
import { useNavigate } from "react-router-dom";

const VoteMeetingSuccess = () => {
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate("/");
  };

  return (
    <div className='flex flex-col pb-[100px] items-center'>
      <Header />
      <div className='flex w-full flex-row justify-start items-center mt-[50px] px-8'>
        <MdArrowBack
          className='h-12 w-12 cursor-pointer'
          onClick={handleGoBack}
        />
      </div>
      <p className='text-2xl font-medium mt-8'>
        Your vote has been registered!
      </p>
      <p className='text-lg font-medium  mt-[50px]'>
        You will get a notification when the final date and time are released
      </p>
      <Button
        className='bg-primary w-[250px] mt-[80px] self-center'
        onClick={() => navigate("/")}
      >
        Return to Home Screen
      </Button>
    </div>
  );
};

export default VoteMeetingSuccess;
